package com.peterparker.service.impl;

import com.peterparker.dao.OwnerBlacklistDAO;
import com.peterparker.model.OwnerBlacklist;
import com.peterparker.model.Owner;
import com.peterparker.service.OwnerBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerBlacklistServiceImpl implements OwnerBlacklistService {

    @Autowired
    private OwnerBlacklistDAO ownerBlacklistDAO;

    @Autowired
    private OwnerService ownerService;

    // 獲取當前登入者的帳號
    private String getCurrentOwnerAccount() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // 授權檢查：通過 OwnerService 的 getOwnerByNo 來驗證當前用戶是否有權限
    private boolean isOwnerAuthorized(Integer ownerNo) {
        Owner currentOwner = ownerService.getOwnerByNo(ownerNo);

        if (currentOwner == null || !currentOwner.getOwnerAccount().equals(getCurrentOwnerAccount())) {
            System.out.println("授權失敗：當前用戶無權限訪問此資源");
            return false;
        }

        System.out.println("授權成功：當前用戶有權限訪問此資源");
        return true;
    }

    @Override
    public List<OwnerBlacklist> getAllBlacklistEntriesByOwnerNo(Integer ownerNo) {
        if (!isOwnerAuthorized(ownerNo)) {
            throw new SecurityException("您無權查看此資料。");
        }
        return ownerBlacklistDAO.findAllByOwnerNo(ownerNo);
    }

    @Override
    public OwnerBlacklist getBlacklistEntryById(Integer ownerNo, Integer ownerBlacklistId) {
        if (!isOwnerAuthorized(ownerNo)) {
            throw new SecurityException("您無權查看此資料。");
        }
        return ownerBlacklistDAO.findById(ownerNo, ownerBlacklistId);
    }

    @Override
    public OwnerBlacklist createBlacklistEntry(OwnerBlacklist ownerBlacklist) {
        Integer ownerNo = ownerBlacklist.getOwnerNo();
        if (!isOwnerAuthorized(ownerNo)) {
            throw new SecurityException("您無權新增此資料。");
        }
        ownerBlacklistDAO.save(ownerBlacklist);
        return ownerBlacklist;
    }

    @Override
    public OwnerBlacklist updateBlacklistEntry(OwnerBlacklist ownerBlacklist) {
        Integer ownerNo = ownerBlacklist.getOwnerNo();
        if (!isOwnerAuthorized(ownerNo)) {
            throw new SecurityException("您無權更新此資料。");
        }
        ownerBlacklistDAO.update(ownerBlacklist);
        return ownerBlacklist;
    }

    @Override
    public void deleteBlacklistEntry(Integer ownerNo, Integer ownerBlacklistId) {
        if (!isOwnerAuthorized(ownerNo)) {
            throw new SecurityException("您無權刪除此資料。");
        }
        ownerBlacklistDAO.delete(ownerNo, ownerBlacklistId);
    }
}
