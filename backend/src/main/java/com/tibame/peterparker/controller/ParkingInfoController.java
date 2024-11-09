package com.tibame.peterparker.controller;

import com.tibame.peterparker.model.ParkingInfo;
import com.tibame.peterparker.service.ParkingInfoService;
import com.tibame.peterparker.dto.ParkingInfoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/owner/owners")
public class ParkingInfoController {

    @Autowired
    private ParkingInfoService parkingInfoService;

    // 獲取會員的所有停車場資訊
    @GetMapping("/{ownerNo}/parkingInfo")
    public ResponseEntity<List<ParkingInfo>> getAllParkingInfoByOwnerNo(@PathVariable Integer ownerNo) {
        List<ParkingInfo> parkingInfos = parkingInfoService.getAllParkingInfoByOwnerNo(ownerNo);

        if (parkingInfos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(parkingInfos);
        }
    }

    // 獲取特定停車場的資訊
    @GetMapping("/{ownerNo}/parkingInfo/{parkingId}")
    public ResponseEntity<ParkingInfo> getParkingInfoByOwnerAndId(@PathVariable Integer ownerNo, @PathVariable Integer parkingId) {
        ParkingInfo parkingInfo = parkingInfoService.getParkingInfoByOwnerAndId(ownerNo, parkingId);

        if (parkingInfo != null) {
            return ResponseEntity.status(HttpStatus.OK).body(parkingInfo);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 創建新的停車場資訊
    @PostMapping("/{ownerNo}/parkingInfo")
    public ResponseEntity<ParkingInfo> createParkingInfo(@PathVariable Integer ownerNo, @RequestBody ParkingInfoRequest parkingInfoRequest) {
        parkingInfoRequest.setOwnerNo(ownerNo);  // 設置 ownerNo
        Integer parkingId = parkingInfoService.createParkingInfo(parkingInfoRequest);
        ParkingInfo parkingInfo = parkingInfoService.getParkingInfoById(parkingId);

        return ResponseEntity.status(HttpStatus.CREATED).body(parkingInfo);
    }

    // 更新特定停車場的資訊
    @PutMapping("/{ownerNo}/parkingInfo/{parkingId}")
    public ResponseEntity<ParkingInfo> updateParkingInfo(@PathVariable Integer ownerNo, @PathVariable Integer parkingId,
                                                         @RequestBody ParkingInfoRequest parkingInfoRequest) {
        ParkingInfo parkingInfo = parkingInfoService.getParkingInfoByOwnerAndId(ownerNo, parkingId);

        if (parkingInfo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        parkingInfoService.updateParkingInfo(ownerNo, parkingId, parkingInfoRequest);
        ParkingInfo updatedParkingInfo = parkingInfoService.getParkingInfoByOwnerAndId(ownerNo, parkingId);

        return ResponseEntity.status(HttpStatus.OK).body(updatedParkingInfo);
    }

    // 刪除特定停車場的資訊
    @DeleteMapping("/{ownerNo}/parkingInfo/{parkingId}")
    public ResponseEntity<?> deleteParkingInfo(@PathVariable Integer ownerNo, @PathVariable Integer parkingId) {
        parkingInfoService.deleteParkingInfoByOwnerAndId(ownerNo, parkingId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 上傳停車場圖片
    @PostMapping("/{ownerNo}/parkingInfo/{parkingId}/uploadImage")
    public ResponseEntity<String> uploadParkingImage(@PathVariable Integer ownerNo,
                                                     @PathVariable Integer parkingId,
                                                     @RequestParam("image") MultipartFile imageFile) {
        try {
            byte[] imageData = imageFile.getBytes();
            parkingInfoService.saveParkingImage(ownerNo, parkingId, imageData);

            return ResponseEntity.status(HttpStatus.OK).body("圖片上傳成功");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("圖片上傳失敗");
        }
    }

    // 獲取停車場圖片
    @GetMapping("/{ownerNo}/parkingInfo/{parkingId}/image")
    public ResponseEntity<byte[]> getParkingImage(@PathVariable Integer ownerNo, @PathVariable Integer parkingId) {
        byte[] imageData = parkingInfoService.getParkingImage(ownerNo, parkingId);

        if (imageData != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)  // 假設圖片為 JPEG 格式
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"parkingImage.jpg\"")
                    .body(imageData);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
