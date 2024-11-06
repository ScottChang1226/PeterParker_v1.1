package com.tibame.peterparker.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tibame.peterparker.dto.OrderDTO;
import com.tibame.peterparker.entity.OrderVO;
import com.tibame.peterparker.service.OrderService;
import com.tibame.peterparker.service.ParkingService;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ParkingService parkingService;

    // 查詢用戶所有訂單
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderVO>> getUserOrders(@PathVariable Integer userId) {
        List<OrderVO> orders = orderService.findOrdersByUserId(userId);
        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // 查詢訂單狀態
    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<OrderVO>> getOrdersByStatus(@PathVariable String statusId) {
        List<OrderVO> orders = orderService.findOrdersByStatusId(statusId);
        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // 創建訂單
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO, HttpSession session) {
        Integer loginUserId = (Integer) session.getAttribute("loginUserId");
        if (loginUserId == null) {
            return new ResponseEntity<>("User is not logged in.", HttpStatus.UNAUTHORIZED);
        }
        orderDTO.setUserId(loginUserId);

        try {
            Integer orderId = orderService.createOrder(orderDTO);
            return new ResponseEntity<>(orderId, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating order: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 根據 orderId 查詢訂單
    @GetMapping("/{orderId}")
    public ResponseEntity<?> findById(@PathVariable Integer orderId) {
        Optional<OrderVO> optional = orderService.findOrderById(orderId);

        if (optional.isPresent()) {
            OrderVO order = optional.get();
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
        return new ResponseEntity<>("Order not found", HttpStatus.BAD_REQUEST);
    }

    // 訂單總金額計算
    @PostMapping("/calculate")
    public ResponseEntity<?> calculateTotalPrice(@RequestBody OrderDTO request) {
        try {
            Integer totalPrice = orderService.calculateTotalPrice(request);
            return new ResponseEntity<>(Map.of("totalPrice", totalPrice), HttpStatus.OK);
        } catch (EntityNotFoundException enfe) {
            return new ResponseEntity<>(enfe.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred during price calculation.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 根據經緯度查找附近的停車場
    @GetMapping("/nearbyParking")
    @Lazy
    public ResponseEntity<?> getNearbyParking(@RequestParam Double latitude, @RequestParam Double longitude) {
        try {
            List<Map<String, Object>> nearbyParking = parkingService.findNearbyParking(latitude, longitude);
            return new ResponseEntity<>(nearbyParking, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error finding nearby parking: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 用關鍵字查找停車場
    @GetMapping("/searchParking")
    @Lazy
    public ResponseEntity<?> searchParking(@RequestParam String keyword) {
        try {
            List<Map<String, Object>> parkingResults = parkingService.searchParkingByKeyword(keyword);
            return new ResponseEntity<>(parkingResults, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error searching parking: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 顯示停車場的剩餘可用車位數量
    @GetMapping("/availableSpaces/{parkingId}")
    @Lazy
    public ResponseEntity<?> getAvailableSpaces(@PathVariable Integer parkingId) {
        try {
            Integer availableSpaces = parkingService.getAvailableSpaces(parkingId);
            return new ResponseEntity<>(Map.of("availableSpaces", availableSpaces), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving available spaces: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
