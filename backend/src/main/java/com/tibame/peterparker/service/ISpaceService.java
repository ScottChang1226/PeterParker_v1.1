package com.tibame.peterparker.service;

import com.tibame.peterparker.dto.SpaceRequest;
import com.tibame.peterparker.entity.Space;

import java.util.List;

public interface ISpaceService {

    List<Space> getAllSpacesByParkingId(Integer ownerNo, Integer parkingId);

    Space getSpaceByParkingIdAndSpaceId(Integer ownerNo, Integer parkingId, Integer spaceId);

    Integer createSpace(Integer ownerNo, SpaceRequest spaceRequest);

    void updateSpace(Integer ownerNo, Integer parkingId, Integer spaceId, SpaceRequest spaceRequest);

    void deleteSpaceByParkingIdAndSpaceId(Integer ownerNo, Integer parkingId, Integer spaceId);

    // 根據 parkingId 獲取所有車位
    List<Space> getAllSpacesByParkingId(Integer parkingId);

    // 根據 parkingId 和 spaceId 獲取特定車位
    Space getSpaceByParkingIdAndSpaceId(Integer parkingId, Integer spaceId);

    // 根據 spaceId 獲取特定車位
    Space getSpaceById(Integer spaceId);

    // 新增車位
    Integer createSpace(SpaceRequest spaceRequest);

    // 更新車位
    void updateSpace(Integer parkingId, Integer spaceId, SpaceRequest spaceRequest);

    // 刪除車位
    void deleteSpaceByParkingIdAndSpaceId(Integer parkingId, Integer spaceId);
}