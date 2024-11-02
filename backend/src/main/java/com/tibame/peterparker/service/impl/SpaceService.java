package com.peterparker.service.impl;

import com.peterparker.dao.ISpaceDAO;
import com.peterparker.dto.SpaceRequest;
import com.peterparker.model.Space;
import com.peterparker.service.ISpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Space savedSpace = spaceDAO.saveSpace(newSpace);
        return savedSpace.getSpaceId();
    }

    @Override
    public void updateSpace(Integer parkingId, Integer spaceId, SpaceRequest spaceRequest) {
        Optional<Space> optionalSpace = spaceDAO.findSpaceByParkingIdAndSpaceId(parkingId, spaceId);

        if (optionalSpace.isPresent()) {
            Space space = optionalSpace.get();
            space.setParkingId(spaceRequest.getParkingId());
            spaceDAO.saveSpace(space);
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
}