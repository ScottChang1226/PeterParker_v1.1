package com.peterparker.dao.impl;

import com.peterparker.dao.OwnerBlacklistDAO;
import com.peterparker.model.OwnerBlacklist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OwnerBlacklistDAOImpl implements OwnerBlacklistDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<OwnerBlacklist> findAllByOwnerNo(Integer ownerNo) {
        String sql = "SELECT * FROM ownerBlacklist WHERE ownerNo = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(OwnerBlacklist.class), ownerNo);
    }

    @Override
    public OwnerBlacklist findById(Integer ownerNo, Integer ownerBlacklistId) {
        String sql = "SELECT * FROM ownerBlacklist WHERE ownerBlacklistId = ? AND ownerNo = ?";
        List<OwnerBlacklist> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(OwnerBlacklist.class), ownerBlacklistId, ownerNo);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public void save(OwnerBlacklist ownerBlacklist) {
        String sql = "INSERT INTO ownerBlacklist (ownerNo, userId) VALUES (?, ?)";
        jdbcTemplate.update(sql, ownerBlacklist.getOwnerNo(), ownerBlacklist.getUserId());
    }

    @Override
    public void update(OwnerBlacklist ownerBlacklist) {
        String sql = "UPDATE ownerBlacklist SET userId = ? WHERE ownerBlacklistId = ? AND ownerNo = ?";
        jdbcTemplate.update(sql, ownerBlacklist.getUserId(), ownerBlacklist.getOwnerBlacklistId(), ownerBlacklist.getOwnerNo());
    }

    @Override
    public void delete(Integer ownerNo, Integer ownerBlacklistId) {
        String sql = "DELETE FROM ownerBlacklist WHERE ownerBlacklistId = ? AND ownerNo = ?";
        jdbcTemplate.update(sql, ownerBlacklistId, ownerNo);
    }
}
