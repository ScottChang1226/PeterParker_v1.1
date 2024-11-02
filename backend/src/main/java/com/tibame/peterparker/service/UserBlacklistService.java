package com.tibame.peterparker.service;

import com.tibame.peterparker.dto.UserBlacklistParkingDTO;
import com.tibame.peterparker.entity.ParkingVO;
import com.tibame.peterparker.entity.UserBlacklistVO;
import com.tibame.peterparker.entity.UserVO;
import com.tibame.peterparker.dao.ParkingRepository;
import com.tibame.peterparker.dao.UserBlacklistRepository;
import com.tibame.peterparker.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserBlacklistService {

    @Autowired
    private UserBlacklistRepository userBlacklistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParkingRepository parkingRepository;

    public void addUserBlacklist(Integer userId, Integer parkingId) {

        UserVO user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found" + userId));
        ParkingVO parking = parkingRepository.findById(parkingId)
                .orElseThrow(() -> new RuntimeException("Parking not found"));

        UserBlacklistVO userBlacklist = new UserBlacklistVO();
        userBlacklist.setUser(user);
        userBlacklist.setParking(parking);

        userBlacklistRepository.save(userBlacklist);
    }

    public void deleteUserBlacklist(Integer userFavouriteId) {
        userBlacklistRepository.deleteByUserBlacklistId(userFavouriteId);
    }

    public List<UserBlacklistParkingDTO> findParkingByUserUserId(Integer userId){

        List<Object[]> results = userBlacklistRepository.findParkingByUserUserId(userId);
        List<UserBlacklistParkingDTO> dtos = results.stream().map(result ->
                new UserBlacklistParkingDTO(
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




}

