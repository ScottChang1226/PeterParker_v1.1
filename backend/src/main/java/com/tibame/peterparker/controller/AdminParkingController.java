package com.tibame.peterparker.controller;

import com.tibame.peterparker.entity.ParkingVO;
import com.tibame.peterparker.service.AdminParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/parking")
public class AdminParkingController {

    @Autowired
    private AdminParkingService adminParkingService;

    // 新增或更新停車場資訊
    @PostMapping("/save")
    public ResponseEntity<ParkingVO> saveParking(@RequestBody ParkingVO parking) {
        ParkingVO savedParking = adminParkingService.saveParking(parking);
        return ResponseEntity.ok(savedParking);
    }

    // 更新停車場資訊
    @PutMapping("/{id}")
    public ResponseEntity<ParkingVO> updateParking(@PathVariable("id") Integer parkingId, @RequestBody ParkingVO parking) {
        Optional<ParkingVO> existingParking = adminParkingService.getParkingById(parkingId);
        if (!existingParking.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        parking.setParkingId(parkingId); // 設定停車場 ID
        ParkingVO updatedParking = adminParkingService.saveParking(parking); // 儲存更新後的停車場
        return ResponseEntity.ok(updatedParking);
    }

    // 根據 ID 查詢停車場資訊
    @GetMapping("/{id}")
    public ResponseEntity<ParkingVO> getParkingById(@PathVariable("id") Integer parkingId) {
        Optional<ParkingVO> parking = adminParkingService.getParkingById(parkingId);
        return parking.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 刪除停車場資訊
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParkingById(@PathVariable("id") Integer parkingId) {
        adminParkingService.deleteParkingById(parkingId);
        return ResponseEntity.noContent().build();
    }

    // 查詢所有停車場資訊
    @GetMapping("/all")
    public ResponseEntity<List<ParkingVO>> getAllParkings() {
        List<ParkingVO> parkings = adminParkingService.getAllParkings();
        return ResponseEntity.ok(parkings);
    }
}
