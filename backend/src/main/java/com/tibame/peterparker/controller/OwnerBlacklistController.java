package com.tibame.peterparker.controller;

import com.tibame.peterparker.model.OwnerBlacklist;
import com.tibame.peterparker.service.OwnerBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owner/owners/{ownerNo}/blacklist")
public class OwnerBlacklistController {

    @Autowired
    private OwnerBlacklistService ownerBlacklistService;


    // 查詢指定業主的所有黑名單
    @GetMapping
    public ResponseEntity<List<OwnerBlacklist>> getAllBlacklistEntries(@PathVariable Integer ownerNo) {
        List<OwnerBlacklist> blacklistEntries = ownerBlacklistService.getAllBlacklistEntriesByOwnerNo(ownerNo);

        if (blacklistEntries.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(blacklistEntries);
        }
    }

    // 查詢特定黑名單項目
    @GetMapping("/{ownerBlacklistId}")
    public ResponseEntity<OwnerBlacklist> getBlacklistEntry(@PathVariable Integer ownerNo, @PathVariable Integer ownerBlacklistId) {
        OwnerBlacklist blacklistEntry = ownerBlacklistService.getBlacklistEntryById(ownerNo, ownerBlacklistId);

        if (blacklistEntry != null) {
            return ResponseEntity.status(HttpStatus.OK).body(blacklistEntry);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 新增黑名單項目
    @PostMapping
    public ResponseEntity<OwnerBlacklist> createBlacklistEntry(@PathVariable Integer ownerNo, @RequestBody OwnerBlacklist ownerBlacklist) {
        ownerBlacklist.setOwnerNo(ownerNo); // 設置 ownerNo 來關聯業主
        OwnerBlacklist newBlacklistEntry = ownerBlacklistService.createBlacklistEntry(ownerBlacklist);

        return ResponseEntity.status(HttpStatus.CREATED).body(newBlacklistEntry);
    }

    // 更新黑名單項目
    @PutMapping("/{ownerBlacklistId}")
    public ResponseEntity<OwnerBlacklist> updateBlacklistEntry(@PathVariable Integer ownerNo, @PathVariable Integer ownerBlacklistId, @RequestBody OwnerBlacklist ownerBlacklist) {
        OwnerBlacklist existingEntry = ownerBlacklistService.getBlacklistEntryById(ownerNo, ownerBlacklistId);

        if (existingEntry == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ownerBlacklist.setOwnerBlacklistId(ownerBlacklistId);
        ownerBlacklist.setOwnerNo(ownerNo); // 確保 ownerNo 正確設置
        OwnerBlacklist updatedEntry = ownerBlacklistService.updateBlacklistEntry(ownerBlacklist);

        return ResponseEntity.status(HttpStatus.OK).body(updatedEntry);
    }

    // 刪除黑名單項目
    @DeleteMapping("/{ownerBlacklistId}")
    public ResponseEntity<?> deleteBlacklistEntry(@PathVariable Integer ownerNo, @PathVariable Integer ownerBlacklistId) {
        ownerBlacklistService.deleteBlacklistEntry(ownerNo, ownerBlacklistId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
