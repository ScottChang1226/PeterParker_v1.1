package com.tibame.peterparker.service;


import com.tibame.peterparker.dto.UserUpdatePasswordDTO;
import com.tibame.peterparker.entity.UserVO;
import com.tibame.peterparker.dao.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class UserService {
    //The service class is where the core processing of data happens.

    private final UserRepository userRepository;
    private EmailService emailService;

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




}
