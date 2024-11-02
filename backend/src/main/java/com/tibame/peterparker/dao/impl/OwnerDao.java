package com.peterparker.dao.impl;

import com.peterparker.dto.OwnerRequest;
import com.peterparker.model.Owner;
import com.peterparker.rowmapper.OwnerRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OwnerDao {

    private final JdbcTemplate jdbcTemplate;

    public OwnerDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    /**
     * 根據帳號查詢 Owner
     */
    public Owner findOwnerByAccount(String ownerAccount) {
        String sql = "SELECT ownerNo, ownerAccount, ownerPassword, ownerName, ownerPhone, ownerGoogleToken " +
                "FROM Owner2 WHERE ownerAccount = ?";
        List<Owner> owners = jdbcTemplate.query(sql, new Object[]{ownerAccount}, new OwnerRowMapper());
        return owners.isEmpty() ? null : owners.get(0);
    }

    /**
     * 創建新的 Owner
     */
    public Integer createOwner(OwnerRequest ownerRequest) {
        String sql = "INSERT INTO owner2 (ownerName, ownerPhone, ownerAccount, ownerPassword) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, ownerRequest.getOwnerName(), ownerRequest.getOwnerPhone(),
                ownerRequest.getOwnerAccount(), ownerRequest.getOwnerPassword());
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
    }

    /**
     * 根據 ownerNo 查詢 Owner
     */
    public Owner getOwnerByNo(Integer ownerNo) {
        String sql = "SELECT * FROM owner2 WHERE ownerNo = ?";
        List<Owner> owners = jdbcTemplate.query(sql, new Object[]{ownerNo}, (rs, rowNum) -> {
            Owner owner = new Owner();
            owner.setOwnerNo(rs.getInt("ownerNo"));
            owner.setOwnerName(rs.getString("ownerName"));
            owner.setOwnerPhone(rs.getString("ownerPhone"));
            owner.setOwnerAccount(rs.getString("ownerAccount"));
            owner.setOwnerPassword(rs.getString("ownerPassword"));
            return owner;
        });
        return owners.isEmpty() ? null : owners.get(0);
    }

    /**
     * 查詢所有 Owner
     */
    public List<Owner> getAllOwners() {
        String sql = "SELECT * FROM owner2";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Owner owner = new Owner();
            owner.setOwnerNo(rs.getInt("ownerNo"));
            owner.setOwnerName(rs.getString("ownerName"));
            owner.setOwnerPhone(rs.getString("ownerPhone"));
            owner.setOwnerAccount(rs.getString("ownerAccount"));
            owner.setOwnerPassword(rs.getString("ownerPassword"));
            return owner;
        });
    }

    /**
     * 更新 Owner
     */
    public void updateOwner(Integer ownerNo, OwnerRequest ownerRequest) {
        String sql = "UPDATE owner2 SET ownerName = ?, ownerPhone = ?, ownerAccount = ?, ownerPassword = ? WHERE ownerNo = ?";
        jdbcTemplate.update(sql, ownerRequest.getOwnerName(), ownerRequest.getOwnerPhone(),
                ownerRequest.getOwnerAccount(), ownerRequest.getOwnerPassword(), ownerNo);
    }

    /**
     * 刪除 Owner
     */
    public void deleteOwnerByNo(Integer ownerNo) {
        String sql = "DELETE FROM owner2 WHERE ownerNo = ?";
        jdbcTemplate.update(sql, ownerNo);
    }

    public Integer findOwnerNoByAccount(String ownerAccount) {
        String sql = "SELECT ownerNo FROM owner2 WHERE ownerAccount = ?"; // 修改表名稱為 "owner2"
        return jdbcTemplate.queryForObject(sql, new Object[]{ownerAccount}, Integer.class);
    }
}
