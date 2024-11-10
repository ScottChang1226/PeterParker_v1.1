package com.tibame.peterparker.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import com.tibame.peterparker.dto.CalculatePriceRequest;
import com.tibame.peterparker.dto.FilterRequest;
import com.tibame.peterparker.dto.ParkingDTO;
import com.tibame.peterparker.entity.ParkingVO;
import com.tibame.peterparker.entity.Space;
import com.tibame.peterparker.service.OrderMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tibame.peterparker.dto.OrderDTO;
import com.tibame.peterparker.entity.OrderVO;
import com.tibame.peterparker.service.OrderService;
import com.tibame.peterparker.service.ParkingService;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;

import java.sql.Date;

//@CrossOrigin(origins = "http://localhost:5500") //本地端佈署用來允許所有跨域
@RestController
@RequestMapping(path = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ParkingService parkingService;

    @Autowired
    private OrderMailService orderMailService;

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
        Integer loginUserId = Integer.parseInt( (String)session.getAttribute("loginUserId"));
        //使用者未登入則阻止成立
        if (loginUserId == null) {
            return new ResponseEntity<>("使用者未登入", HttpStatus.UNAUTHORIZED);
        }

        // 如果用戶未登入，使用預設 ID 999
//        if (loginUserId == null) {
//            loginUserId = 1; // 預設的未登入用戶 ID
//        }

        orderDTO.setUserId(loginUserId);


        try {
            // 創建訂單
            Integer orderId = orderService.createOrder(orderDTO);

            // 獲取用戶的 email（userAccount）
            String userEmail = orderService.getUserAccountByUserId(loginUserId);

            // 發送訂單完成郵件
            OrderMailService orderMailService = new OrderMailService();
            orderMailService.sendMail(userEmail, "您的訂單已創建成功！訂單號：" + orderId);

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

    // 訂單總金額計算***
    @PostMapping("/calculate")
    public ResponseEntity<?> calculateTotalPrice(@RequestBody CalculatePriceRequest calculatePriceRequest) {
        try {
            // 通過 ParkingDTO 信息計算總金額
            Optional<ParkingVO> parkingInfoOptional = parkingService.getParkingInfoById(calculatePriceRequest.getParkingId());

            if (parkingInfoOptional.isEmpty()) {
                return new ResponseEntity<>("並未找到此停車場: " + calculatePriceRequest.getParkingId(), HttpStatus.NOT_FOUND);
            }

            ParkingVO parkingInfo = parkingInfoOptional.get();
            Integer totalPrice = orderService.calculateTotalPrice(parkingService.convertToDTO(parkingInfo), calculatePriceRequest.getOrderStartTime(), calculatePriceRequest.getOrderEndTime());

            return new ResponseEntity<>(Map.of("totalPrice", totalPrice), HttpStatus.OK);
        } catch (IllegalArgumentException iae) {
            return new ResponseEntity<>(iae.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException enfe) {
            return new ResponseEntity<>(enfe.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("在計算總金額時發生錯誤: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }







    // 根據經緯度查找附近的停車場
    @PostMapping("/nearbyParking")
    public ResponseEntity<?> getNearbyParking(@RequestBody Map<String, Object> request) {
        try {
            // 嘗試將 latitude, longitude, radius 從請求中獲取，並轉換為 Double 類型
            Double latitude = null;
            Double longitude = null;
            Double radius = null;

            if (request.get("latitude") instanceof Number) {
                latitude = ((Number) request.get("latitude")).doubleValue();
            }
            if (request.get("longitude") instanceof Number) {
                longitude = ((Number) request.get("longitude")).doubleValue();
            }
            if (request.get("radius") instanceof Number) {
                radius = ((Number) request.get("radius")).doubleValue();
            }

            // 確保參數不為空
            if (latitude == null || longitude == null || radius == null) {
                throw new IllegalArgumentException("Missing or invalid latitude, longitude, or radius");
            }

            // 呼叫 service 查找附近的停車場
            List<Map<String, Object>> nearbyParking = parkingService.findNearbyParking(latitude, longitude, radius);
            return new ResponseEntity<>(nearbyParking, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error finding nearby parking: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // 用關鍵字查找停車場
    @GetMapping("/searchParking")
    @Lazy
    public ResponseEntity<?> searchParking(@RequestParam(value = "keyword", required = false) String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return new ResponseEntity<>("Keyword parameter is missing or invalid", HttpStatus.BAD_REQUEST);
            }

            List<Map<String, Object>> parkingResults = parkingService.searchParkingByKeyword(keyword);
            return new ResponseEntity<>(parkingResults, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error searching parking: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 顯示停車場的剩餘可用車位數量
    @GetMapping("/availableSpaces/{parkingId}")
    @Lazy
    public ResponseEntity<?> getAvailableSpaces(
            @PathVariable Integer parkingId,
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.util.Date date,
            @RequestParam(value = "timeRange", required = false) String timeRange) {
        try {
            // 如果未傳入 date，則設置為當天日期
            if (date == null) {
                Calendar calendar = Calendar.getInstance();
                date = calendar.getTime();
            }

            // 將 java.util.Date 轉換為 java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            // 如果未傳入 timeRange，則設置為當下到三小時後
            if (timeRange == null || timeRange.isEmpty()) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                String startTime = timeFormat.format(calendar.getTime());

                calendar.add(Calendar.HOUR_OF_DAY, 3);
                String endTime = timeFormat.format(calendar.getTime());

                timeRange = startTime + "-" + endTime; // 例如 "14:00-17:00"
            }

            // 調用 Service 的方法獲取結果
            Integer availableSpaces = parkingService.getAvailableSpaces(parkingId, sqlDate, timeRange);
            return new ResponseEntity<>(Map.of("availableSpaces", availableSpaces), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving available spaces: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getParkingListings")
    public ResponseEntity<?> getParkingListings(@RequestBody Map<String, Object> bounds) {
        try {
            Double northEastLat = (Double) ((Map<String, Object>) bounds.get("northEast")).get("lat");
            Double northEastLng = (Double) ((Map<String, Object>) bounds.get("northEast")).get("lng");
            Double southWestLat = (Double) ((Map<String, Object>) bounds.get("southWest")).get("lat");
            Double southWestLng = (Double) ((Map<String, Object>) bounds.get("southWest")).get("lng");

            List<Map<String, Object>> parkingListings = parkingService.findParkingByBounds(northEastLat, northEastLng, southWestLat, southWestLng);
            return new ResponseEntity<>(parkingListings, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching parking listings: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/filteredParkingListings")
    public ResponseEntity<?> getFilteredParkingListings(@RequestBody FilterRequest filterRequest) {
        try {
            List<ParkingDTO> parkingList = parkingService.getFilteredParkingListings(filterRequest);
            if (parkingList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(parkingList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching filtered parking listings: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 根據 parkingId 查詢停車場資訊
    @GetMapping("/parking/{parkingId}")
    public ResponseEntity<?> getParkingInfoById(@PathVariable Integer parkingId) {
        try {
            Optional<ParkingVO> optional = parkingService.getParkingInfoById(parkingId);
            if (optional.isPresent()) {
                ParkingDTO parkingDTO = parkingService.convertToDTO(optional.get());
                return new ResponseEntity<>(parkingDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("未找到停車場", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching parking info: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
