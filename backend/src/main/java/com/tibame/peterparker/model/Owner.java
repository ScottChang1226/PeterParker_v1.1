package com.peterparker.model;

public class Owner {

    private String ownerAccount;
    private String ownerPassword;
    private Integer ownerNo;
    private String ownerName;
    private String ownerPhone;
    private String ownerGoogleToken;

    // 無參數構造函數
    public Owner() {}

    // 有參數構造函數
    public Owner(String ownerAccount, String ownerPassword) {
        this.ownerAccount = ownerAccount;
        this.ownerPassword = ownerPassword;
    }

    // Getter 和 Setter 方法
    public String getOwnerAccount() {
        return ownerAccount;
    }

    public void setOwnerAccount(String ownerAccount) {
        this.ownerAccount = ownerAccount;
    }

    public String getOwnerPassword() {
        return ownerPassword;
    }

    public void setOwnerPassword(String ownerPassword) {
        this.ownerPassword = ownerPassword;
    }

    public Integer getOwnerNo() {
        return ownerNo;
    }

    public void setOwnerNo(Integer ownerNo) {
        this.ownerNo = ownerNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getOwnerGoogleToken() {
        return ownerGoogleToken;
    }

    public void setOwnerGoogleToken(String ownerGoogleToken) {
        this.ownerGoogleToken = ownerGoogleToken;
    }
}
