package com.tibame.peterparker.service.impl;

import com.tibame.peterparker.dao.ISpaceDAO;
import com.tibame.peterparker.dao.OrderRepository;
import com.tibame.peterparker.dto.SpaceRequest;
import com.tibame.peterparker.model.OrderVO;
import com.tibame.peterparker.model.Space;
import com.tibame.peterparker.service.ISpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SpaceService implements ISpaceService {

    @Autowired
    private ISpaceDAO spaceDAO;

    @Override
    public List<Space> getAllSpacesByParkingId(Integer ownerNo, Integer parkingId) {
        return List.of();
    }

    @Override
    public Space getSpaceByParkingIdAndSpaceId(Integer ownerNo, Integer parkingId, Integer spaceId) {
        return null;
    }

    @Override
    public Integer createSpace(Integer ownerNo, SpaceRequest spaceRequest) {
        return 0;
    }

    @Override
    public void updateSpace(Integer ownerNo, Integer parkingId, Integer spaceId, SpaceRequest spaceRequest) {

    }

    @Override
    public void deleteSpaceByParkingIdAndSpaceId(Integer ownerNo, Integer parkingId, Integer spaceId) {

    }

    @Override
    public List<Space> getAllSpacesByParkingId(Integer parkingId) {
        return spaceDAO.findAllSpacesByParkingId(parkingId);
    }

    @Override
    public Space getSpaceByParkingIdAndSpaceId(Integer parkingId, Integer spaceId) {
        Optional<Space> space = spaceDAO.findSpaceByParkingIdAndSpaceId(parkingId, spaceId);
        return space.orElse(null);
    }

    @Override
    public Integer createSpace(SpaceRequest spaceRequest) {
        Space newSpace = new Space();
        newSpace.setParkingId(spaceRequest.getParkingId());
        newSpace.setSpaceNo(spaceRequest.getSpaceNo()); // 設置 spaceNo
        Space savedSpace = spaceDAO.saveSpace(newSpace);
        return savedSpace.getSpaceId();
    }

    @Override
    public void updateSpace(Integer parkingId, Integer spaceId, SpaceRequest spaceRequest) {
        Optional<Space> optionalSpace = spaceDAO.findSpaceByParkingIdAndSpaceId(parkingId, spaceId);

        if (optionalSpace.isPresent()) {
            Space space = optionalSpace.get();
            space.setSpaceNo(spaceRequest.getSpaceNo()); // 更新 spaceNo
            space.setParkingId(spaceRequest.getParkingId()); // 確保 parkingId 也更新
            spaceDAO.saveSpace(space); // 保存更新後的 Space
        } else {
            throw new RuntimeException("Space not found with id: " + spaceId);
        }
    }

    @Override
    public void deleteSpaceByParkingIdAndSpaceId(Integer parkingId, Integer spaceId) {
        spaceDAO.deleteSpaceByParkingIdAndSpaceId(parkingId, spaceId);
    }

    // 使用 getSpaceById 方法
    public Space getSpaceById(Integer spaceId) {
        Optional<Space> space = spaceDAO.getSpaceById(spaceId);
        return space.orElse(null);
    }
    @Autowired
    private OrderRepository orderRepository;

    // 新增方法以查找指定 spaceId 且 orderStartTime 大於當前時間的訂單

    public List<OrderVO> getUpcomingOrdersBySpaceId(Integer spaceId) {
        return orderRepository.findUpcomingOrdersBySpaceId(spaceId);
    }


}