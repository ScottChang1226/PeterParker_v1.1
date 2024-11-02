package com.tibame.peterparker.service;


import com.tibame.peterparker.dto.UserOrderInfoDTO;
import com.tibame.peterparker.dto.UserProfileDTO;
import com.tibame.peterparker.dto.UserUpdatePasswordDTO;
import com.tibame.peterparker.entity.UserVO;
import com.tibame.peterparker.dao.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserService {
    //The service class is where the core processing of data happens.

    private final UserRepository userRepository;
    private UserEmailService userEmailService;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isUserAccountTaken(String userAccount) {
        return userRepository.existsByUserAccount(userAccount);
    }

    public UserVO findUserByUserAccountAndUserPassword(String userAccount, String userPassword) {
        return userRepository.findByUserAccountAndUserPassword(userAccount, userPassword);
    }


    public UserVO findUserByUserId(int userId) {
        return userRepository.findByUserId(userId);
    }

    public String insertUser(UserVO userVO) {
        if (userRepository.existsByUserAccount(userVO.getUserAccount())) {
            return "User already exists";
        }
        // Save user via JPA (ID will auto-increment)
        userRepository.insertUser(userVO.getUserName(),userVO.getUserPhone(), userVO.getUserAccount(),userVO.getUserPassword(),userVO.getCarNumber());

        return "User added successfully";
    }

    public int updateUser(UserVO userVO) {
        return userRepository.updateUser(userVO.getUserName(),userVO.getUserPhone(),userVO.getUserAccount(),userVO.getUserPassword(),userVO.getCarNumber(), userVO.getUserId());
    }

    public int updatePassword(UserUpdatePasswordDTO updatePasswordDTO){
        String secretKey = "ResetPasswordSecretKeyForOurBelovedProjectPeterParker";
        String newUserPassword = updatePasswordDTO.getUserPassword();
        String updatePasswordToken = updatePasswordDTO.getUpdatePasswordToken();

        // Parse the token and extract claims (data stored in the token)
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey) // Secret key used to sign the token
                .parseClaimsJws(updatePasswordToken) // Token to parse
                .getBody();

        // Extract the subject (userAccount)
        String userAccount = claims.getSubject();

        return userRepository.updateUserPassword(userAccount,newUserPassword);

    }

    public UserVO findByUserAccount(String userAccount) {
        return userRepository.findByUserAccount(userAccount);
    };

    public int updateGoogleToken(String userAccount, String googleToken) {
        return userRepository.updateGoogleToken(userAccount, googleToken);
    }

    public int insertGoogleUser(String userName, String userAccount, String userPhone, String carNumber, String googleToken ) {
        return userRepository.insertGoogleUser(userName, userAccount, userPhone, carNumber, googleToken);
    }

    public void uploadPhoto(UserProfileDTO userProfileDTO) throws IOException {

            Integer userId = userProfileDTO.getUserId();
            System.out.println("userId received in backend"+userId);
            byte[] profilePhoto = userProfileDTO.getProfilePhoto().getBytes(); // Convert file to byte array
            userRepository.updateProfilePhoto(userId, profilePhoto);
    }

    public byte[] getProfilePhoto(Integer userId) {
        return userRepository.getProfilePhoto(userId);
    }


    public List<UserOrderInfoDTO> getOrderParkingInfo(String statusId, Integer userId) {
        List<Object[]> results = userRepository.findOrderParkingInfoByStatusIdAndUserId(statusId, userId);

        // Map the raw Object[] results to the DTOs
        return results.stream()
                .map(result -> new UserOrderInfoDTO(
                        (Integer) result[0], // parkingImg
                        (String) result[1], // parkingName
                        (Integer) result[2], // orderId
                        (Integer) result[3], // spaceId
                        (Integer) result[4], // userId
                        (String) result[5], // statusId
                        (Timestamp) result[6], // orderStartTime
                        (Timestamp) result[7], // orderEndTime
                        (Integer) result[8], // orderTotalIncome
                        (String) result[9], // userComment
                        (Timestamp) result[10] // orderModified
                ))
                .collect(Collectors.toList());
    }





}
