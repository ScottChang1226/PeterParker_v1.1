package com.peterparker.service.impl;

import com.peterparker.dao.impl.OwnerDao;
import com.peterparker.dto.OwnerRequest;
import com.peterparker.model.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerService implements UserDetailsService {

    @Autowired
    private OwnerDao ownerDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String ownerAccount) throws UsernameNotFoundException {
        Owner owner = ownerDao.findOwnerByAccount(ownerAccount);
        if (owner == null) {
            throw new UsernameNotFoundException("Owner not found with account: " + ownerAccount);
        }
        return User.withUsername(owner.getOwnerAccount())
                .password(owner.getOwnerPassword())
                .roles("USER")
                .build();
    }

    /**
     * 獲取當前登入者的帳號
     */
    private String getCurrentOwnerAccount() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private boolean isOwnerAuthorized(Integer ownerNo) {
        Owner currentOwner = ownerDao.findOwnerByAccount(getCurrentOwnerAccount());

        // 添加日誌以幫助調試
        if (currentOwner == null) {
            System.out.println("未找到當前登入者的帳號對應的 Owner 記錄");
            return false;
        }

        if (currentOwner.getOwnerNo() == null) {
            System.out.println("當前登入者的 OwnerNo 為 null");
            return false;
        }

        boolean isAuthorized = currentOwner.getOwnerNo().equals(ownerNo);
        System.out.println("授權檢查結果: " + isAuthorized);
        return isAuthorized;
    }
    /**
     * 創建新的 Owner
     */
    public Integer createOwner(OwnerRequest ownerRequest) {
        String encodedPassword = passwordEncoder.encode(ownerRequest.getOwnerPassword());
        ownerRequest.setOwnerPassword(encodedPassword);
        return ownerDao.createOwner(ownerRequest);
    }

    /**
     * 根據 OwnerNo 查詢 Owner，僅允許查詢自己的資料
     */
    public Owner getOwnerByNo(Integer ownerNo) {
        if (!isOwnerAuthorized(ownerNo)) {
            throw new SecurityException("You are not authorized to view this data.");
        }
        return ownerDao.getOwnerByNo(ownerNo);
    }

    /**
     * 更新 Owner 資訊，僅允許更新自己的資料
     */
    public void updateOwner(Integer ownerNo, OwnerRequest ownerRequest) {
        if (!isOwnerAuthorized(ownerNo)) {
            throw new SecurityException("You are not authorized to update this data.");
        }
        if (ownerRequest.getOwnerPassword() != null && !ownerRequest.getOwnerPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(ownerRequest.getOwnerPassword());
            ownerRequest.setOwnerPassword(encodedPassword);
        }
        ownerDao.updateOwner(ownerNo, ownerRequest);
    }

    /**
     * 刪除 Owner，僅允許刪除自己的資料
     */
    public void deleteOwnerByNo(Integer ownerNo) {
        if (!isOwnerAuthorized(ownerNo)) {
            throw new SecurityException("You are not authorized to delete this data.");
        }
        ownerDao.deleteOwnerByNo(ownerNo);
    }

    public List<Owner> getAllOwners() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            throw new SecurityException("You are not authorized to view all owners.");
        }
        return ownerDao.getAllOwners();
    }

    public Integer getOwnerNoByAccount(String ownerAccount) {
        return ownerDao.findOwnerNoByAccount(ownerAccount);
    }
}
