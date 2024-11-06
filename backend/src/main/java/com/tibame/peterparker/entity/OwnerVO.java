package com.tibame.peterparker.entity;

import javax.persistence.*;

@Entity
@Table(name = "owner")
public class OwnerVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ownerNo")
    private Integer ownerNo;

    @Column(name = "ownerName", nullable = false)
    private String ownerName;

    @Column(name = "ownerPhone", nullable = false)
    private String ownerPhone;

    @Column(name = "ownerAccount", nullable = false, unique = true)
    private String ownerAccount;

    @Column(name = "ownerPassword", nullable = false)
    private String ownerPassword;

    @Column(name = "ownerGoogleToken", columnDefinition = "TEXT")
    private String ownerGoogleToken;

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

	public String getOwnerGoogleToken() {
		return ownerGoogleToken;
	}

	public void setOwnerGoogleToken(String ownerGoogleToken) {
		this.ownerGoogleToken = ownerGoogleToken;
	}

    
}
