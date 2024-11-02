package com.tibame.peterparker.dao;

import com.tibame.peterparker.entity.OrderVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderVO, Integer> {

    // 根據狀態查找訂單
    List<OrderVO> findByStatusId(String statusId);

    //根據用戶ID和狀態找訂單
    List<OrderVO> findByStatusIdAndUser_UserId(String statusId, String userId);

    // 根據用戶 ID 查找所有訂單（修改方法名稱以符合命名規則）
    List<OrderVO> findByUser_UserId(Integer userId);

    // 查找在指定時間段內已被預訂的停車位
    @Query("SELECT o FROM OrderVO o WHERE o.spaceId = :spaceId AND " +
            "((o.orderStartTime <= :startTime AND o.orderEndTime >= :startTime) OR " +
            "(o.orderStartTime <= :endTime AND o.orderEndTime >= :endTime) OR " +
            "(o.orderStartTime >= :startTime AND o.orderEndTime <= :endTime))")
    List<OrderVO> findConflictingOrders(Integer spaceId, Timestamp startTime, Timestamp endTime);

    // 修改訂單狀態
    @Modifying
    @Query("UPDATE OrderVO o SET o.statusId = :statusId WHERE o.orderId = :orderId")
    int updateOrderStatus(Integer orderId, String statusId);

    // 刪除訂單
    void deleteById(Integer orderId);
}
