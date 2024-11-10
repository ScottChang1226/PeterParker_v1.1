package com.tibame.peterparker.service;

import com.tibame.peterparker.entity.ParkingVO;
import com.tibame.peterparker.dao.AdminParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminParkingService {

    @Autowired
    private AdminParkingRepository adminParkingRepository;

    // 新增或更新停車場資訊
    public ParkingVO saveParking(ParkingVO parking) {
        return adminParkingRepository.save(parking); // 若有 ID，為更新操作；無 ID，為新增操作
    }

    // 根據 ID 查詢停車場資訊
    public Optional<ParkingVO> getParkingById(Integer parkingId) {
        return adminParkingRepository.findById(parkingId);
    }

    // 刪除停車場資訊
    public void deleteParkingById(Integer parkingId) {
        adminParkingRepository.deleteById(parkingId);
    }

    // 查詢所有停車場資訊
    public List<ParkingVO> getAllParkings() {
        return adminParkingRepository.findAll();
    }
}
