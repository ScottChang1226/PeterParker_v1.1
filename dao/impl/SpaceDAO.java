package com.tibame.peterparker.dao.impl;

import com.tibame.peterparker.dao.ISpaceDAO;
import com.tibame.peterparker.model.Space;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class SpaceDAO implements ISpaceDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 定義 RowMapper 用於映射查詢結果到 Space 實體
    private static class SpaceRowMapper implements RowMapper<Space> {
        @Override
        public Space mapRow(ResultSet rs, int rowNum) throws SQLException {
            Space space = new Space();
            space.setSpaceId(rs.getInt("spaceId"));
            space.setParkingId(rs.getInt("parkingId"));
            space.setSpaceNo(rs.getString("spaceNo"));
            return space;
        }
    }

    @Override
    public List<Space> findAllSpacesByParkingId(Integer parkingId) {
        String sql = "SELECT * FROM Space WHERE parkingId = ?";
        return jdbcTemplate.query(sql, new SpaceRowMapper(), parkingId);
    }

    @Override
    public Optional<Space> findSpaceByParkingIdAndSpaceId(Integer parkingId, Integer spaceId) {
        String sql = "SELECT * FROM Space WHERE parkingId = ? AND spaceId = ?";
        List<Space> spaces = jdbcTemplate.query(sql, new SpaceRowMapper(), parkingId, spaceId);
        return spaces.stream().findFirst();
    }

    @Override
    public Space saveSpace(Space space) {
        if (space.getSpaceId() == null) {
            // 插入新記錄
            String sql = "INSERT INTO Space (parkingId, spaceNo) VALUES (?, ?)";
            jdbcTemplate.update(sql, space.getParkingId(), space.getSpaceNo());
            // 獲取自動生成的 spaceId，這裡需要再執行一次查詢以獲取最新記錄
            return jdbcTemplate.queryForObject("SELECT * FROM Space WHERE parkingId = ? ORDER BY spaceId DESC LIMIT 1", new SpaceRowMapper(), space.getParkingId());
        } else {
            // 更新現有記錄
            String sql = "UPDATE Space SET parkingId = ?, spaceNo = ? WHERE spaceId = ?";
            jdbcTemplate.update(sql, space.getParkingId(), space.getSpaceNo(), space.getSpaceId());
            return space;
        }
    }

    @Override
    public void deleteSpaceByParkingIdAndSpaceId(Integer parkingId, Integer spaceId) {
        String sql = "DELETE FROM Space WHERE parkingId = ? AND spaceId = ?";
        jdbcTemplate.update(sql, parkingId, spaceId);
    }

    @Override
    public Optional<Space> getSpaceById(Integer spaceId) {
        String sql = "SELECT * FROM Space WHERE spaceId = ?";
        List<Space> spaces = jdbcTemplate.query(sql, new SpaceRowMapper(), spaceId);
        return spaces.stream().findFirst();
    }



}
