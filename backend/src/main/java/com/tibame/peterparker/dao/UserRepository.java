package com.tibame.peterparker.dao;

import com.tibame.peterparker.entity.UserVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserVO, Integer> {
    boolean existsByUserAccount(String userAccount);
    UserVO findByUserAccountAndUserPassword(String userAccount, String userPassword);
    UserVO findByUserId(Integer userId);
    UserVO findByUserAccount(String userAccount);

    @Modifying
    @Query(value = "UPDATE users u SET u.userPassword = :userPassword WHERE u.userAccount = :userAccount", nativeQuery = true)
    int updateUserPassword(String userAccount, String userPassword);


    @Modifying
    @Query(value = "INSERT INTO users (userName, userAccount, userPassword, userPhone, carNumber) " +
            "VALUES (:userName, :userAccount, :userPassword, :userPhone, :carNumber)", nativeQuery = true)
    int insertUser(String userName, String userPhone, String userAccount, String userPassword, String carNumber);


    @Modifying
    @Query(value = "UPDATE users SET userName = :userName, userPhone = :userPhone, userAccount = :userAccount, " +
            "userPassword = :userPassword, carNumber = :carNumber WHERE userId = :userId", nativeQuery = true)
    int updateUser(String userName, String userPhone,
                   String userAccount, String userPassword,
                   String carNumber, int userId);

    @Modifying
    @Query(value = "UPDATE users SET googleToken = :googleToken WHERE userAccount = :userAccount", nativeQuery = true)
    int updateGoogleToken(String userAccount, String googleToken);

    @Modifying
    @Query(value = "INSERT INTO users (userName, userAccount, userPhone, carNumber, googleToken) VALUES (:userName, :userAccount, :userPhone, :carNumber, :googleToken)", nativeQuery = true)
    int insertGoogleUser(@Param("userName") String userName,
                         @Param("userAccount") String userAccount,
                         @Param("userPhone") String userPhone,
                         @Param("carNumber") String carNumber,
                         @Param("googleToken") String googleToken);

    @Modifying
    @Query(value = "UPDATE users SET profilePhoto = :profilePhoto WHERE userId = :userId", nativeQuery = true)
    int updateProfilePhoto(@Param("userId") Integer userId, @Param("profilePhoto") byte[] profilePhoto);

    @Query(value = "SELECT profilePhoto FROM users WHERE userId = :userId", nativeQuery = true)
    byte[] getProfilePhoto(Integer userId);

    @Query(value = "SELECT pi.parkingId, pi.parkingName, o.orderId, o.spaceId, o.userId, o.statusId, " +
            "o.orderStartTime, o.orderEndTime, o.orderTotalIncome, o.userComment, o.orderModified " +
            "FROM orderinfo o " +
            "JOIN space s ON o.spaceId = s.spaceId " +
            "JOIN parkingInfo pi ON s.parkingId = pi.parkingId " +
            "WHERE o.statusId = :statusId AND o.userId = :userId",
            nativeQuery = true)
    List<Object[]> findOrderParkingInfoByStatusIdAndUserId(@Param("statusId") String statusId,
                                                           @Param("userId") Integer userId);


    @Modifying
    @Query(value= "UPDATE orderinfo o SET o.userComment = :userComment WHERE o.orderId = :orderId", nativeQuery=true)
    public int updateOrderComment(@Param("userComment")String userComment, @Param("orderId")Integer orderId);

    //獲取userAccount作為寄信mail
    Optional<UserVO> findById(Integer userId);

}
