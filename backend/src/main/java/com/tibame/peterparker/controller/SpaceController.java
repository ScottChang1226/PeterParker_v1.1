package com.tibame.peterparker.controller;

import com.tibame.peterparker.dto.SpaceRequest;
import com.tibame.peterparker.model.OrderVO;
import com.tibame.peterparker.model.Space;
import com.tibame.peterparker.service.impl.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("owner/owners")
public class SpaceController {

    @Autowired
    private SpaceService spaceService;

    // 获取指定停车场的所有车位
    @GetMapping("/{ownerNo}/parkingInfo/{parkingId}/spaces")
    public ResponseEntity<List<Space>> getAllSpacesByParkingId(@PathVariable Integer ownerNo, @PathVariable Integer parkingId) {
        List<Space> spaces = spaceService.getAllSpacesByParkingId(parkingId);

        if (spaces.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(spaces);
        }
    }

    // 获取指定停车场中特定的车位信息
    @GetMapping("/{ownerNo}/parkingInfo/{parkingId}/spaces/{spaceId}")
    public ResponseEntity<Space> getSpaceByParkingIdAndSpaceId(@PathVariable Integer ownerNo, @PathVariable Integer parkingId, @PathVariable Integer spaceId) {
        Space space = spaceService.getSpaceByParkingIdAndSpaceId(parkingId, spaceId);

        if (space != null) {
            return ResponseEntity.status(HttpStatus.OK).body(space);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 新增车位
    @PostMapping("/{ownerNo}/parkingInfo/{parkingId}/spaces")
    public ResponseEntity<Space> createSpace(@PathVariable Integer ownerNo, @PathVariable Integer parkingId, @RequestBody SpaceRequest spaceRequest) {
        spaceRequest.setParkingId(parkingId); // 关联停车场
        Integer spaceId = spaceService.createSpace(spaceRequest);
        Space space = spaceService.getSpaceById(spaceId);

        return ResponseEntity.status(HttpStatus.CREATED).body(space);
    }

    // 更新车位信息
    @PutMapping("/{ownerNo}/parkingInfo/{parkingId}/spaces/{spaceId}")
    public ResponseEntity<Space> updateSpace(@PathVariable Integer ownerNo, @PathVariable Integer parkingId, @PathVariable Integer spaceId, @RequestBody SpaceRequest spaceRequest) {
        Space space = spaceService.getSpaceByParkingIdAndSpaceId(parkingId, spaceId);

        if (space == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        spaceRequest.setParkingId(parkingId); // 关联停车场
        spaceService.updateSpace(parkingId, spaceId, spaceRequest);
        Space updatedSpace = spaceService.getSpaceByParkingIdAndSpaceId(parkingId, spaceId);

        return ResponseEntity.status(HttpStatus.OK).body(updatedSpace);
    }

    // 删除车位
    @DeleteMapping("/{ownerNo}/parkingInfo/{parkingId}/spaces/{spaceId}")
    public ResponseEntity<?> deleteSpace(@PathVariable Integer ownerNo, @PathVariable Integer parkingId, @PathVariable Integer spaceId) {
        spaceService.deleteSpaceByParkingIdAndSpaceId(parkingId, spaceId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    // 新增 API 端點來查詢符合條件的訂單資料
    @GetMapping("/{ownerNo}/parkingInfo/{parkingId}/spaces/{spaceId}/upcoming-orders")
    public ResponseEntity<List<OrderVO>> getUpcomingOrdersBySpaceId(@PathVariable Integer spaceId) {
        List<OrderVO> orders = spaceService.getUpcomingOrdersBySpaceId(spaceId);

        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(orders);
        }
    }
}
