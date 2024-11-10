package com.tibame.peterparker.service;


import com.tibame.peterparker.dao.OrderRepository;
import com.tibame.peterparker.dto.UserOrderInfoDTO;
import com.tibame.peterparker.dto.UserProfileDTO;
import com.tibame.peterparker.dto.UserUpdatePasswordDTO;
import com.tibame.peterparker.entity.UserVO;
import com.tibame.peterparker.dao.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private UserEmailService userEmailService;

    @Autowired
    private OrderRepository orderRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 使用者帳號是否已登入，回傳是與否
    public boolean isUserAccountTaken(String userAccount) {
        return userRepository.existsByUserAccount(userAccount);
    }

    // 使用者登入
    public UserVO findUserByUserAccountAndUserPassword(String userAccount, String userPassword) {
        return userRepository.findByUserAccountAndUserPassword(userAccount, userPassword);
    }

    // 從JWT裝入裡擷取使用者ID，透過使用者ID拿取UserVO
    public UserVO findUserByUserId(int userId) {
        return userRepository.findByUserId(userId);
    }

    // 新使用者註冊
    public String insertUser(UserVO userVO) {
        if (userRepository.existsByUserAccount(userVO.getUserAccount())) {
            return "User already exists";
        }
        // Save user via JPA (ID will auto-increment)
        userRepository.insertUser(userVO.getUserName(),userVO.getUserPhone(), userVO.getUserAccount(),userVO.getUserPassword(),userVO.getCarNumber());

        return "User added successfully";
    }

    // 更新使用者資訊
    public int updateUser(UserVO userVO) {
        return userRepository.updateUser(userVO.getUserName(),userVO.getUserPhone(),userVO.getUserAccount(),userVO.getUserPassword(),userVO.getCarNumber(), userVO.getUserId());
    }

    // 使用者更新密碼
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

    // 確認使用者email是否已經註冊過，並回傳使用者的資料
    public UserVO findByUserAccount(String userAccount) {
        return userRepository.findByUserAccount(userAccount);
    };

    // 已經是會員的使用者串接 Google 登入
    public int updateGoogleToken(String userAccount, String googleToken) {
        return userRepository.updateGoogleToken(userAccount, googleToken);
    }

    // Google 登入者創建帳號
    public int insertGoogleUser(String userName, String userAccount, String userPhone, String carNumber, String googleToken ) {
        return userRepository.insertGoogleUser(userName, userAccount, userPhone, carNumber, googleToken);
    }

    // 使用者上傳圖片
    public void uploadPhoto(UserProfileDTO userProfileDTO) throws IOException {

            Integer userId = userProfileDTO.getUserId();
            System.out.println("userId received in backend"+userId);
            byte[] profilePhoto = userProfileDTO.getProfilePhoto().getBytes(); // Convert file to byte array
            userRepository.updateProfilePhoto(userId, profilePhoto);
    }

    // 取得使用者的圖片
    public byte[] getProfilePhoto(Integer userId) {
        return userRepository.getProfilePhoto(userId);
    }

    // 列出使用者所有訂單依據狀態
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

    // 取消執行中的訂單
    public int cancelExistingOrder(Integer orderId, String statusId) {
       return orderRepository.updateOrderStatus(orderId,statusId);
    }

    // 更改訂單用戶評價
    public int updateUserComment(String userComment, Integer orderId){
        return userRepository.updateOrderComment(userComment,orderId);
    }

}
