package com.tibame.peterparker.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="users")
public class UserVO implements Serializable {

    //Field
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="userId")
    private Integer userId;

    @Column (name="userName", nullable = false)
    private String userName;

    @Column (name="userAccount", nullable = false)
    private String userAccount;


    @Column (name="userPassword")
    private String userPassword;

    @Column (name="userPhone", nullable = false)
    private String userPhone;

    @Column (name="carNumber", nullable = false)
    private String carNumber;

    @Column(name="googleToken", columnDefinition = "TEXT")
    private String googleToken;

    @Lob  // Defines a column for large objects



//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<UserFavouriteVO> userFavourites;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName( String userName) {
        this.userName = userName;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }


    public String getGoogleToken() {
        return googleToken;
    }


    public void setGoogleToken(String googleToken) {
        this.googleToken = googleToken;
    }



    public UserVO(){
    }

    public UserVO(String userName, String userAccount, String userPassword, String userPhone, String carNumber) {
        this.userName = userName;
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.userPhone = userPhone;
        this.carNumber = carNumber;
    }

    public UserVO(int userId, String userName, String userAccount, String userPassword, String userPhone, String carNumber) {
        this.userName = userName;
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.userPhone = userPhone;
        this.carNumber = carNumber;
    }
}