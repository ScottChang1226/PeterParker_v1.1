package com.tibame.peterparker.service;

import com.tibame.peterparker.entity.OrderVO;
import com.tibame.peterparker.dao.AdminOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.sql.Timestamp;

@Service
public class AdminOrderService {

    @Autowired
    private AdminOrderRepository adminOrderRepository;

    // 新增或更新訂單
    public OrderVO saveOrder(OrderVO order) {
        if (order.getOrderId() != null) {
            // 確認訂單是否存在
            OrderVO existingOrder = adminOrderRepository.findById(order.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID " + order.getOrderId()));
            
            // 更新現有訂單的字段
            existingOrder.setStatusId(order.getStatusId());
            existingOrder.setUserComment(order.getUserComment());
            existingOrder.setOrderTotalIncome(order.getOrderTotalIncome());
            existingOrder.setOrderEndTime(order.getOrderEndTime());
            existingOrder.setOrderModified(new Timestamp(System.currentTimeMillis())); // 設置更新的時間

            // 儲存更新後的訂單
            return adminOrderRepository.save(existingOrder);
        } else {
            // 如果是新增的訂單，直接儲存
            return adminOrderRepository.save(order);
        }
    }

    // 根據 ID 查詢訂單
    public Optional<OrderVO> getOrderById(Integer orderId) {
        return adminOrderRepository.findById(orderId);
    }

    // 刪除訂單
    public void deleteOrderById(Integer orderId) {
        adminOrderRepository.deleteById(orderId);
    }

    // 查詢所有訂單
    public List<OrderVO> getAllOrders() {
        return adminOrderRepository.findAll();
    }
    
    // 計算訂單數量
    public int countOrders() {
        int count = (int) adminOrderRepository.count();
        System.out.println("Order Count: " + count);  // 加入這一行來確認資料庫的回應
        return count;
    }
}
