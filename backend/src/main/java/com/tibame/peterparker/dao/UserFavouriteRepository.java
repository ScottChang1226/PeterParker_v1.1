package com.tibame.peterparker.dao;

import com.tibame.peterparker.entity.UserFavouriteVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFavouriteRepository extends JpaRepository <UserFavouriteVO, Integer> {
        List<UserFavouriteVO> findByUserUserId(Integer userId);

        @Query(value = "SELECT uf.userFavouriteId, p.parkingId, p.capacity, p.parkingName, p.parkingRegion, p.parkingLocation, " +
                "p.parkingLong, p.parkingLat, p.holidayHourlyRate, p.workdayHourlyRate, p.ownerNo " +
                "FROM userFavourite uf " +
                "JOIN parkinginfo p ON uf.parkingId = p.parkingId " +
                "WHERE uf.userId = :userId", nativeQuery = true)
        List<Object[]> findParkingByUserUserId(@Param("userId") Integer userId);


        @Query(value = "SELECT userFavouriteId FROM userFavourite WHERE userId = :userId AND parkingId = :parkingId", nativeQuery = true)
        Integer findByUserIdAndParkingId(@Param("userId") Integer userId, @Param("parkingId") Integer parkingId);

        void deleteByUserFavouriteId(Integer userFavouriteId);

}
