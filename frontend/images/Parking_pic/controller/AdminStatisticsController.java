package com.tibame.peterparker.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tibame.peterparker.service.AdminOrderService;
import com.tibame.peterparker.service.AdminOwnerService;
import com.tibame.peterparker.service.AdminUserService;

@RestController
@RequestMapping("/api/statistics")
public class AdminStatisticsController {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private AdminOwnerService adminOwnerService;

    @Autowired
    private AdminOrderService adminOrderService;

    @GetMapping("/counts")
    public ResponseEntity<Map<String, Integer>> getCounts() {
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        
        int userCount = adminUserService.countUsers();
        int ownerCount = adminOwnerService.countOwners();
        int orderCount = adminOrderService.countOrders();

        Map<String, Integer> counts = new HashMap<>();
        counts.put("userCount", userCount);
        counts.put("ownerCount", ownerCount);
        counts.put("orderCount", orderCount);

        return ResponseEntity.ok(counts);
    }
}

