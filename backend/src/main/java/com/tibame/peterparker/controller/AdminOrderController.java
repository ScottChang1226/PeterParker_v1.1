package com.tibame.peterparker.controller;

import com.tibame.peterparker.entity.OrderVO;
import com.tibame.peterparker.service.AdminOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class AdminOrderController {

    @Autowired
    private AdminOrderService adminOrderService;

    // 新增或更新訂單
    @PostMapping("/save")
    public ResponseEntity<OrderVO> saveOrder(@RequestBody OrderVO order) {
        OrderVO savedOrder = adminOrderService.saveOrder(order);
        return ResponseEntity.ok(savedOrder);
    }
    
    // 更新訂單
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable("id") Integer orderId, @RequestBody OrderVO order) {
        try {
            Optional<OrderVO> existingOrder = adminOrderService.getOrderById(orderId);
            if (!existingOrder.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
            }

            if (order.getStatusId() == null || order.getStatusId().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Status ID cannot be null or empty");
            }

            OrderVO orderToUpdate = existingOrder.get();
            orderToUpdate.setStatusId(order.getStatusId());
            orderToUpdate.setOrderModified(new Timestamp(System.currentTimeMillis()));

            OrderVO updatedOrder = adminOrderService.saveOrder(orderToUpdate);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update order");
        }
    }



    // 根據 ID 查詢訂單
    @GetMapping("/{id}")
    public ResponseEntity<OrderVO> getOrderById(@PathVariable("id") Integer orderId) {
        Optional<OrderVO> order = adminOrderService.getOrderById(orderId);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 刪除訂單
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable("id") Integer orderId) {
    	adminOrderService.deleteOrderById(orderId);
        return ResponseEntity.noContent().build();
    }

    // 查詢所有訂單
    @GetMapping("/all")
    public ResponseEntity<List<OrderVO>> getAllOrders() {
        List<OrderVO> orders = adminOrderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}
