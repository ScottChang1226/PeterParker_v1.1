package com.peterparker.service;

import com.peterparker.model.OwnerBlacklist;

import java.util.List;

public interface OwnerBlacklistService {
    List<OwnerBlacklist> getAllBlacklistEntriesByOwnerNo(Integer ownerNo);
    OwnerBlacklist getBlacklistEntryById(Integer ownerNo, Integer ownerBlacklistId);
    OwnerBlacklist createBlacklistEntry(OwnerBlacklist ownerBlacklist);
    OwnerBlacklist updateBlacklistEntry(OwnerBlacklist ownerBlacklist);
    void deleteBlacklistEntry(Integer ownerNo, Integer ownerBlacklistId);
}
