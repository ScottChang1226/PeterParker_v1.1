package com.tibame.peterparker.controller;

import com.tibame.peterparker.entity.OwnerVO;
import com.tibame.peterparker.service.AdminOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/owner")
public class AdminOwnerController {

    @Autowired
    private AdminOwnerService adminOwnerService;

    // 新增或更新業主
    @PostMapping("/save")
    public ResponseEntity<OwnerVO> saveOwner(@RequestBody OwnerVO owner) {
        OwnerVO savedOwner = adminOwnerService.saveOwner(owner);
        return ResponseEntity.ok(savedOwner);
    }

    // 根據 ID 更新業主
    @PutMapping("/{ownerNo}")
    public ResponseEntity<OwnerVO> updateOwner(@PathVariable("ownerNo") Integer ownerNo, @RequestBody OwnerVO owner) {
        Optional<OwnerVO> existingOwner = adminOwnerService.getOwnerById(ownerNo);
        if (!existingOwner.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        owner.setOwnerNo(ownerNo);  // 確保 ownerNo 的名稱一致
        OwnerVO updatedOwner = adminOwnerService.saveOwner(owner);
        return ResponseEntity.ok(updatedOwner);
    }

    // 根據 ID 查詢業主
    @GetMapping("/{ownerNo}")
    public ResponseEntity<OwnerVO> getOwnerById(@PathVariable("ownerNo") Integer ownerId) {
        Optional<OwnerVO> owner = adminOwnerService.getOwnerById(ownerId);
        return owner.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 刪除業主
    @DeleteMapping("/{ownerNo}")
    public ResponseEntity<Void> deleteOwnerById(@PathVariable("ownerNo") Integer ownerNo) {
        adminOwnerService.deleteOwnerById(ownerNo);
        return ResponseEntity.noContent().build();
    }

    // 查詢所有業主
    @GetMapping("/all")
    public ResponseEntity<List<OwnerVO>> getAllOwners() {
        List<OwnerVO> owners = adminOwnerService.getAllOwners();
        return ResponseEntity.ok(owners);
    }
    
    // 查詢所有業主及其停車場資訊
//    @GetMapping("/allWithParking")
//    public ResponseEntity<List<OwnerWithParkingDTO>> getAllOwnersWithParking() {
//        List<OwnerWithParkingDTO> owners = adminOwnerService.getAllOwnersWithParking();
//        return ResponseEntity.ok(owners);
//    }
    
    // 根據 ownerNo 查詢業主及其停車場資訊
//    @GetMapping("/withParking/{ownerNo}")
//    public ResponseEntity<OwnerWithParkingDTO> getOwnerWithParking(@PathVariable("ownerNo") Integer ownerNo) {
//        Optional<OwnerWithParkingDTO> ownerWithParking = adminOwnerService.getOwnerWithParkingById(ownerNo);
//        return ownerWithParking.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
}
