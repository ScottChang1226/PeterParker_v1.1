package com.tibame.peterparker.controller;

import com.tibame.peterparker.entity.UserVO;
import com.tibame.peterparker.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class AdminUserController {

	@Autowired
	private AdminUserService userService;

	// 新增或更新用戶
	@PostMapping("/save")
	public ResponseEntity<UserVO> saveUser(@RequestBody UserVO user) {
		System.out.println(user);

		UserVO savedUser = userService.saveUser(user);
		return ResponseEntity.ok(savedUser);
	}

	// 更新用戶
	@PutMapping("/{id}")
	public ResponseEntity<UserVO> updateUser(@PathVariable("id") Integer userId, @RequestBody UserVO user) {
		// 檢查用戶是否存在
		Optional<UserVO> existingUser = userService.getUserById(userId);
		if (!existingUser.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		// 檢查 userAccount 是否為空，為了防止 Hibernate 驗證錯誤
	    if (user.getUserAccount() == null || user.getUserAccount().isEmpty()) {
	        throw new IllegalArgumentException("User account cannot be null or empty");
	    }
		
		// 更新資料
		user.setUserId(userId); // 設置用戶 ID
		UserVO updatedUser = userService.saveUser(user); // 保存更新後的用戶
		return ResponseEntity.ok(updatedUser);
		
	}

	// 根據 ID 查詢用戶
	@GetMapping("/{id}")
	public ResponseEntity<UserVO> getUserById(@PathVariable("id") Integer userId) {
		Optional<UserVO> user = userService.getUserById(userId);
		return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	// 刪除用戶
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUserById(@PathVariable("id") Integer userId) {
		userService.deleteUserById(userId);
		return ResponseEntity.noContent().build();
	}

	// 查詢所有用戶
	@GetMapping("/all")
	public ResponseEntity<List<UserVO>> getAllUsers() {
		List<UserVO> users = userService.getAllUsers();
		return ResponseEntity.ok(users);
	}
}
