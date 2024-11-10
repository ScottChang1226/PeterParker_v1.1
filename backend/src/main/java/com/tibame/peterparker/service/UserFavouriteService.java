package com.tibame.peterparker.service;

import com.tibame.peterparker.dto.UserFavouriteParkingDTO;
import com.tibame.peterparker.entity.ParkingVO;
import com.tibame.peterparker.entity.UserFavouriteVO;
import com.tibame.peterparker.entity.UserVO;
import com.tibame.peterparker.dao.ParkingRepository;
import com.tibame.peterparker.dao.UserFavouriteRepository;
import com.tibame.peterparker.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserFavouriteService {

    @Autowired
    private UserFavouriteRepository userFavouriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParkingRepository parkingRepository;

    public void addUserFavourite(Integer userId, Integer parkingId) {

        UserVO user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found" + userId));
        ParkingVO parking = parkingRepository.findById(parkingId)
                .orElseThrow(() -> new RuntimeException("Parking not found"));

        UserFavouriteVO userFavourite = new UserFavouriteVO();
        userFavourite.setUser(user);
        userFavourite.setParking(parking);

        userFavouriteRepository.save(userFavourite);
    }

    public void deleteUserFavourite(Integer userFavouriteId) {
        userFavouriteRepository.deleteByUserFavouriteId(userFavouriteId);
    }

    public List<UserFavouriteParkingDTO> findParkingByUserUserId(Integer userId){

        List<Object[]> results = userFavouriteRepository.findParkingByUserUserId(userId);
        List<UserFavouriteParkingDTO> dtos = results.stream().map(result ->
                new UserFavouriteParkingDTO(
                        (Integer) result[0],
                        (Integer) result[1],
                        (Integer) result[2],
                        (String) result[3],
                        (String) result[4],
                        (String) result[5],
                        (Double) result[6],
                        (Double) result[7],
                        (Integer) result[8],
                        (Integer) result[9],
                        (Integer) result[10]
                )
        ).collect(Collectors.toList());

        return dtos;
    }

    public boolean findExistingFavouriteId (Integer userId, Integer parkingId){
        Integer favouriteId = userFavouriteRepository.findByUserIdAndParkingId(userId, parkingId);
        if (favouriteId == null){
            return false;
        }
        return true;
    }




}

