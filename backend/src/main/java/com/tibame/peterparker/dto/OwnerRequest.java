package com.peterparker.dto;

public class OwnerRequest {

    private String ownerName;

    private String ownerPhone;

    private String ownerAccount;

    private String ownerPassword;

    private String ownerGoogleToken;


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

    public Object getOwnerGoogleToken() {
        return ownerGoogleToken;
    }


    public void setOwnerGoogleToken(String ownerGoogleToken) {
        this.ownerGoogleToken = ownerGoogleToken;
    }
}
