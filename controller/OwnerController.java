package com.tibame.peterparker.controller;

import com.tibame.peterparker.model.Owner;
import com.tibame.peterparker.dto.OwnerRequest;
import com.tibame.peterparker.service.impl.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/owner")
public class OwnerController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private OwnerService ownerService;

    /**
     * 登入
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Owner owner, HttpSession session) {
        try {
            // 使用 ownerAccount 和 ownerPassword 進行身份認證
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(owner.getOwnerAccount(), owner.getOwnerPassword())
            );
            System.out.println("Current Session ID: " + session.getId());

            // 如果身份驗證成功，將認證資訊保存到 SecurityContext 中
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 將使用者資訊保存到 HttpSession 中
            session.setAttribute("loggedInUser", owner.getOwnerAccount());

            // 取得 ownerNo 並存入 Session 中（假設透過 ownerService 取得 ownerNo）
            Integer ownerNo = ownerService.getOwnerNoByAccount(owner.getOwnerAccount());
            session.setAttribute("ownerNo", ownerNo);


            return ResponseEntity.ok("登入成功！Session ID: " + session.getId());

        } catch (AuthenticationException e) {
            // 身份驗證失敗，返回401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("身份驗證失敗");
        }
    }

    /**
     * 獲取當前登入使用者資訊
     */
    @GetMapping("/profile")
    public ResponseEntity<String> getProfile(HttpSession session) {
        // 從 Session 中取得登入的使用者資訊
        String loggedInUser = (String) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            return ResponseEntity.ok("當前登入使用者：" + loggedInUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登入");
        }
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("已成功登出");
    }


    /**
     * 創建新的 Owner
     */
    @PostMapping("/createowner")
    public ResponseEntity<Owner> createOwner(@RequestBody OwnerRequest ownerRequest) {
        Integer ownerNo = ownerService.createOwner(ownerRequest);
        Owner newOwner = ownerService.getOwnerByNo(ownerNo);
        return ResponseEntity.status(HttpStatus.CREATED).body(newOwner);
    }

    /**
     * 查詢所有 Owner
     */
    @GetMapping("/owners")
    public ResponseEntity<List<Owner>> getAllOwners() {
        List<Owner> owners = ownerService.getAllOwners();
        return ResponseEntity.ok(owners);
    }

    /**
     * 根據 ownerNo 獲取 Owner 資訊
     */
    @GetMapping("/owners/{ownerNo}")
    public ResponseEntity<Owner> getOwner(@PathVariable Integer ownerNo) {
        Owner owner = ownerService.getOwnerByNo(ownerNo);
        return owner != null ? ResponseEntity.ok(owner) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * 更新 Owner
     */
    @PutMapping("/owners/{ownerNo}")
    public ResponseEntity<Owner> updateOwner(@PathVariable Integer ownerNo, @RequestBody OwnerRequest ownerRequest) {
        Owner existingOwner = ownerService.getOwnerByNo(ownerNo);
        if (existingOwner == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ownerService.updateOwner(ownerNo, ownerRequest);
        Owner updatedOwner = ownerService.getOwnerByNo(ownerNo);
        return ResponseEntity.ok(updatedOwner);
    }

    /**
     * 刪除 Owner
     */
    @DeleteMapping("/owners/{ownerNo}")
    public ResponseEntity<Void> deleteOwner(@PathVariable Integer ownerNo) {
        ownerService.deleteOwnerByNo(ownerNo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ownerNo")
    public ResponseEntity<Integer> getOwnerNoFromSession(HttpSession session) {
        Integer ownerNo = (Integer) session.getAttribute("ownerNo");
        System.out.println("Session ID: " + session.getId());
        if (ownerNo != null) {
            return ResponseEntity.ok(ownerNo);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


}
