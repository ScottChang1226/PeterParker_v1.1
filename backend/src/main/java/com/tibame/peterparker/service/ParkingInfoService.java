package com.peterparker.service;

import com.peterparker.dto.ParkingInfoRequest;
import com.peterparker.model.ParkingInfo;

import java.util.List;

public interface ParkingInfoService {

    ParkingInfo getParkingInfoByOwnerAndId(Integer ownerNo, Integer parkingId);
    Integer createParkingInfo(ParkingInfoRequest parkingInfoRequest);
    void updateParkingInfo(Integer ownerNo, Integer parkingId, ParkingInfoRequest parkingInfoRequest);
    void deleteParkingInfoByOwnerAndId(Integer ownerNo, Integer parkingId);
    List<ParkingInfo> getAllParkingInfoByOwnerNo(Integer ownerNo); // 新增查询所有停车场信息的方法
    ParkingInfo getParkingInfoById(Integer parkingId);
    void saveParkingImage(Integer ownerNo, Integer parkingId, byte[] imageData);
    byte[] getParkingImage(Integer ownerNo, Integer parkingId); // 新增获取图片数据的方法


}
