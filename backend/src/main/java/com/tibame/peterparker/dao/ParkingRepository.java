package com.tibame.peterparker.dao;

import com.tibame.peterparker.entity.ParkingVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingRepository extends JpaRepository<ParkingVO, Integer> {

    // 根據經緯度範圍查找停車場
    List<ParkingVO> findByParkingLatBetweenAndParkingLongBetween(Double latStart, Double latEnd, Double longStart, Double longEnd);

    // 使用自定義 JPQL 查詢篩選停車場，根據類型和距離
    @Query("SELECT p FROM ParkingVO p WHERE (:types IS NULL) AND (:distance IS NULL OR FUNCTION('calculate_distance', :lat, :lng, p.parkingLat, p.parkingLong) <= :distance)")
    List<ParkingVO> findFilteredParkingListings(
            @Param("types") List<String> types,
            @Param("lat") Double latitude,
            @Param("lng") Double longitude,
            @Param("distance") Double distance
    );
}
