package com.tibame.peterparker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "admin") // 對應 admin 表
public class AdminVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adminID")  // 主鍵對應 admin_ID
    private Integer adminId;

	@Column(name = "adminUsername")  // 對應 admin_username 列
    private String adminUsername;
    
    @Column(name = "adminPassword")  // 對應 admin_password 列
    private String adminPassword;
    
    @Column(name = "adminName")  // 對應 admin_name 列
    private String adminName;
    
    @Column(name = "adminStatus")  // 對應 admin_status 列
    private String adminStatus;
    
    @Lob
    @Column(name = "profileImg", columnDefinition = "LONGBLOB")  // 對應 profile_img 列
    private byte[] profileImg; 
    
	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public String getAdminUsername() {
		return adminUsername;
	}

	public void setAdminUsername(String adminUsername) {
		this.adminUsername = adminUsername;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getAdminStatus() {
		return adminStatus;
	}

	public void setAdminStatus(String adminStatus) {
		this.adminStatus = adminStatus;
	}

	public byte[] getProfileImg() {
		return profileImg;
	}

	public void setProfileImg(byte[] profileImg) {
		this.profileImg = profileImg;
	}
}

