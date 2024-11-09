package com.tibame.peterparker.service.impl;

import com.tibame.peterparker.dao.ParkingInfoDao;
import com.tibame.peterparker.dto.ParkingInfoRequest;
import com.tibame.peterparker.model.ParkingInfo;
import com.tibame.peterparker.model.Owner;
import com.tibame.peterparker.service.ParkingInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingInfoServiceImpl implements ParkingInfoService {

    @Autowired
    private ParkingInfoDao parkingInfoDao;

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
    public ParkingInfo getParkingInfoByOwnerAndId(Integer ownerNo, Integer parkingId) {
        if (!isOwnerAuthorized(ownerNo)) {
            throw new SecurityException("您無權查看此資料。");
        }
        return parkingInfoDao.getParkingInfoByOwnerAndId(ownerNo, parkingId);
    }

    @Override
    public Integer createParkingInfo(ParkingInfoRequest parkingInfoRequest) {
        Integer ownerNo = parkingInfoRequest.getOwnerNo();
        if (!isOwnerAuthorized(ownerNo)) {
            throw new SecurityException("您無權新增此資料。");
        }
        return parkingInfoDao.createParkingInfo(parkingInfoRequest);
    }

    @Override
    public void updateParkingInfo(Integer ownerNo, Integer parkingId, ParkingInfoRequest parkingInfoRequest) {
        if (!isOwnerAuthorized(ownerNo)) {
            throw new SecurityException("您無權更新此資料。");
        }
        parkingInfoDao.updateParkingInfo(ownerNo, parkingId, parkingInfoRequest);
    }

    @Override
    public void deleteParkingInfoByOwnerAndId(Integer ownerNo, Integer parkingId) {
        if (!isOwnerAuthorized(ownerNo)) {
            throw new SecurityException("您無權刪除此資料。");
        }
        parkingInfoDao.deleteParkingInfoByOwnerAndId(ownerNo, parkingId);
    }

    @Override
    public List<ParkingInfo> getAllParkingInfoByOwnerNo(Integer ownerNo) {
        if (!isOwnerAuthorized(ownerNo)) {
            throw new SecurityException("您無權查看此資料。");
        }
        return parkingInfoDao.getAllParkingInfoByOwnerNo(ownerNo);
    }

    @Override
    public ParkingInfo getParkingInfoById(Integer parkingId) {
        // 此方法不使用 ownerNo 授權檢查，僅根據 parkingId 獲取資料
        return parkingInfoDao.getParkingInfoById(parkingId);
    }

    @Override
    public void saveParkingImage(Integer ownerNo, Integer parkingId, byte[] imageData) {
        if (!isOwnerAuthorized(ownerNo)) {
            throw new SecurityException("您無權上傳此圖片。");
        }
        parkingInfoDao.saveParkingImage(ownerNo, parkingId, imageData);
    }

    @Override
    public byte[] getParkingImage(Integer ownerNo, Integer parkingId) {
        if (!isOwnerAuthorized(ownerNo)) {
            throw new SecurityException("您無權查看此圖片。");
        }
        return parkingInfoDao.getParkingImage(ownerNo, parkingId);
    }
}
