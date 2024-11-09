package com.tibame.peterparker.dao;

import com.tibame.peterparker.model.Space;

import java.util.List;
import java.util.Optional;

public interface ISpaceDAO {

    List<Space> findAllSpacesByParkingId(Integer parkingId);

    Optional<Space> findSpaceByParkingIdAndSpaceId(Integer parkingId, Integer spaceId);

    Space saveSpace(Space space);

    void deleteSpaceByParkingIdAndSpaceId(Integer parkingId, Integer spaceId);

    // 新增這個方法
    Optional<Space> getSpaceById(Integer spaceId);
}
