package com.tibame.peterparker.dao;

import com.tibame.peterparker.entity.SpaceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface SpaceStatusRepository extends JpaRepository<SpaceStatus, Integer> {

    // 查找指定 spaceId 並且在指定時間段內有衝突的狀態
    List<SpaceStatus> findBySpaceIdAndOrderStartTimeLessThanAndOrderEndTimeGreaterThan(
            Integer spaceId, Timestamp endTime, Timestamp startTime);
}
