package com.peterparker.dao;

import com.peterparker.model.OwnerBlacklist;

import java.util.List;

public interface OwnerBlacklistDAO {
    List<OwnerBlacklist> findAllByOwnerNo(Integer ownerNo);
    OwnerBlacklist findById(Integer ownerNo, Integer ownerBlacklistId);
    void save(OwnerBlacklist ownerBlacklist);
    void update(OwnerBlacklist ownerBlacklist);
    void delete(Integer ownerNo, Integer ownerBlacklistId);
}
