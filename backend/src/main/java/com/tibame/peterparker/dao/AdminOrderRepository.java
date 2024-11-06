package com.tibame.peterparker.dao;

import com.tibame.peterparker.entity.OrderVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminOrderRepository extends JpaRepository<OrderVO, Integer> {
    // 可以在這裡添加自定義查詢方法
}
