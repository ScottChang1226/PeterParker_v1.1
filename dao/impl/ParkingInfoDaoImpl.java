package com.tibame.peterparker.dao.impl;

import com.tibame.peterparker.dao.ParkingInfoDao;
import com.tibame.peterparker.dto.ParkingInfoRequest;
import com.tibame.peterparker.model.ParkingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ParkingInfoDaoImpl implements ParkingInfoDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public ParkingInfo getParkingInfoByOwnerAndId(Integer ownerNo, Integer parkingId) {
        String sql = "SELECT * FROM ParkingInfo WHERE ownerNo = ? AND parkingId = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{ownerNo, parkingId}, (rs, rowNum) -> mapRowToParkingInfo(rs));
    }

    @Override
    public Integer createParkingInfo(ParkingInfoRequest parkingInfoRequest) {
        String sql = "INSERT INTO ParkingInfo (capacity, parkingName, parkingRegion, parkingLocation, parkingLong, parkingLat, holidayHourlyRate, workdayHourlyRate, parkingImg, ownerNo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, parkingInfoRequest.getCapacity(), parkingInfoRequest.getParkingName(),
                parkingInfoRequest.getParkingRegion(), parkingInfoRequest.getParkingLocation(),
                parkingInfoRequest.getParkingLong(), parkingInfoRequest.getParkingLat(),
                parkingInfoRequest.getHolidayHourlyRate(), parkingInfoRequest.getWorkdayHourlyRate(),
                parkingInfoRequest.getParkingImg(), parkingInfoRequest.getOwnerNo());
    }

    @Override
    public void updateParkingInfo(Integer ownerNo, Integer parkingId, ParkingInfoRequest parkingInfoRequest) {
        String sql = "UPDATE ParkingInfo SET capacity = ?, parkingName = ?, parkingRegion = ?, parkingLocation = ?, " +
                "parkingLong = ?, parkingLat = ?, holidayHourlyRate = ?, workdayHourlyRate = ? " +
                "WHERE ownerNo = ? AND parkingId = ?";

        jdbcTemplate.update(sql,
                parkingInfoRequest.getCapacity(),
                parkingInfoRequest.getParkingName(),
                parkingInfoRequest.getParkingRegion(),
                parkingInfoRequest.getParkingLocation(),
                parkingInfoRequest.getParkingLong(),
                parkingInfoRequest.getParkingLat(),
                parkingInfoRequest.getHolidayHourlyRate(),
                parkingInfoRequest.getWorkdayHourlyRate(),
                ownerNo,
                parkingId
        );
    }

    @Override
    public void deleteParkingInfoByOwnerAndId(Integer ownerNo, Integer parkingId) {
        String sql = "DELETE FROM ParkingInfo WHERE ownerNo = ? AND parkingId = ?";
        jdbcTemplate.update(sql, ownerNo, parkingId);
    }

    @Override
    public List<ParkingInfo> getAllParkingInfoByOwnerNo(Integer ownerNo) {
        String sql = "SELECT * FROM ParkingInfo WHERE ownerNo = ?";
        return jdbcTemplate.query(sql, new Object[]{ownerNo}, (rs, rowNum) -> mapRowToParkingInfo(rs));
    }

    @Override
    public ParkingInfo getParkingInfoById(Integer parkingId) {
        return null;
    }

    @Override
    public void saveParkingImage(Integer ownerNo, Integer parkingId, byte[] imageData) {
        String sql = "UPDATE ParkingInfo SET parkingImg = ? WHERE ownerNo = ? AND parkingId = ?";
        jdbcTemplate.update(sql, imageData, ownerNo, parkingId);
    }

    @Override
    public byte[] getParkingImage(Integer ownerNo, Integer parkingId) {
        String sql = "SELECT parkingImg FROM ParkingInfo WHERE ownerNo = ? AND parkingId = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{ownerNo, parkingId}, byte[].class);
    }

    private ParkingInfo mapRowToParkingInfo(ResultSet rs) throws SQLException {
        ParkingInfo parkingInfo = new ParkingInfo();
        parkingInfo.setParkingId(rs.getInt("parkingId"));
        parkingInfo.setCapacity(rs.getInt("capacity"));
        parkingInfo.setParkingName(rs.getString("parkingName"));
        parkingInfo.setParkingRegion(rs.getString("parkingRegion"));
        parkingInfo.setParkingLocation(rs.getString("parkingLocation"));
        parkingInfo.setParkingLong(rs.getDouble("parkingLong"));
        parkingInfo.setParkingLat(rs.getDouble("parkingLat"));
        parkingInfo.setHolidayHourlyRate(rs.getInt("holidayHourlyRate"));
        parkingInfo.setWorkdayHourlyRate(rs.getInt("workdayHourlyRate"));
        parkingInfo.setParkingImg(rs.getBytes("parkingImg"));
        parkingInfo.setOwnerNo(rs.getInt("ownerNo"));
        return parkingInfo;
    }
}
