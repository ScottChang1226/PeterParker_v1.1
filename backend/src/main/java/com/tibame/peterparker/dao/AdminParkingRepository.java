package com.tibame.peterparker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tibame.peterparker.entity.ParkingVO;

public interface AdminParkingRepository extends JpaRepository<ParkingVO, Integer> {
    // 可根據需要添加自訂查詢方法
}
