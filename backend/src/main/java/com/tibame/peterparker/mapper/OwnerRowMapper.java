package com.peterparker.rowmapper;

import com.peterparker.model.Owner;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OwnerRowMapper implements RowMapper<Owner> {
    @Override
    public Owner mapRow(ResultSet rs, int rowNum) throws SQLException {
        Owner owner = new Owner();
        owner.setOwnerNo(rs.getInt("ownerNo")); // 確保映射 ownerNo
        owner.setOwnerAccount(rs.getString("ownerAccount"));
        owner.setOwnerPassword(rs.getString("ownerPassword"));
        owner.setOwnerName(rs.getString("ownerName"));
        owner.setOwnerPhone(rs.getString("ownerPhone"));
        owner.setOwnerGoogleToken(rs.getString("ownerGoogleToken"));
        return owner;
    }
}
