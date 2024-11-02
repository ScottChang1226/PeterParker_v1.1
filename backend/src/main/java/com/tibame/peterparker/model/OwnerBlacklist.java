package com.peterparker.model;

import javax.persistence.*;

@Entity
@Table(name = "ownerBlacklist")
public class OwnerBlacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ownerBlacklistId;

    private Integer ownerNo;
    private Integer userId;

    // Getters and Setters
    public Integer getOwnerBlacklistId() {
        return ownerBlacklistId;
    }

    public void setOwnerBlacklistId(Integer ownerBlacklistId) {
        this.ownerBlacklistId = ownerBlacklistId;
    }

    public Integer getOwnerNo() {
        return ownerNo;
    }

    public void setOwnerNo(Integer ownerNo) {
        this.ownerNo = ownerNo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
