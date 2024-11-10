package com.tibame.peterparker.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tibame.peterparker.entity.AdminVO;
import com.tibame.peterparker.service.AdminService;

@CrossOrigin(origins = "http://localhost:5500") // 設定允許的來源
@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;

	// 登入功能
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
		try {
			String token = adminService.login(loginData.get("username"), loginData.get("password"));
			Optional<AdminVO> adminOpt = adminService.findByUsername(loginData.get("username"));
			
			if (adminOpt.isPresent()) {
				AdminVO admin = adminOpt.get();
				Map<String, Object> response = new HashMap<>();
				response.put("token", token);
				response.put("adminId", admin.getAdminId()); // 返回 adminId
				return ResponseEntity.ok(response);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}

	// 新增或更新用戶
	@PostMapping("/save")
	public ResponseEntity<AdminVO> saveUser(@RequestBody AdminVO admin) {
		AdminVO savedUser = adminService.saveAdmin(admin);
		return ResponseEntity.ok(savedUser);
	}

	// 更新管理員
	@PutMapping("/{id}")
	public ResponseEntity<AdminVO> updateUser(@PathVariable("id") Integer adminId, @RequestBody AdminVO user) {
		Optional<AdminVO> existingUser = adminService.getUserById(adminId);
		if (!existingUser.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		AdminVO existingAdmin = existingUser.get();

		if (user.getAdminUsername() == null || user.getAdminUsername().isEmpty()) {
			throw new IllegalArgumentException("Admin username cannot be null or empty");
		}

		// 更新資料
		existingAdmin.setAdminName(user.getAdminName());
		existingAdmin.setAdminUsername(user.getAdminUsername());
		existingAdmin.setAdminPassword(user.getAdminPassword());
		existingAdmin.setAdminStatus(user.getAdminStatus());

		if (user.getProfileImg() != null) {
			existingAdmin.setProfileImg(user.getProfileImg());
		}

		AdminVO updatedUser = adminService.saveAdmin(existingAdmin);
		return ResponseEntity.ok(updatedUser);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AdminVO> getUserById(@PathVariable("id") Integer adminId) {
		Optional<AdminVO> user = adminService.getUserById(adminId);
		return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUserById(@PathVariable("id") Integer adminId) {
		adminService.deleteUserById(adminId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/all")
	public ResponseEntity<List<AdminVO>> getAllUsers() {
		List<AdminVO> users = adminService.getAllUsers();
		return ResponseEntity.ok(users);
	}

	// 抓取圖片
	@GetMapping("/image/{adminId}")
	public ResponseEntity<byte[]> getProfileImage(@PathVariable Integer adminId) {
		Optional<AdminVO> admin = adminService.getUserById(adminId);

		if (admin.isPresent() && admin.get().getProfileImg() != null) {
			byte[] imageBytes = admin.get().getProfileImg();
			return ResponseEntity.ok()
					.header("Content-Type", "image/jpeg")
					.header("Content-Disposition", "inline; filename=\"profile.jpg\"")
					.body(imageBytes);
		} else {
			try (InputStream defaultImageStream = getClass().getResourceAsStream("/static/images/dashboard-avatar.jpg")) {
				if (defaultImageStream != null) {
					byte[] defaultImageBytes = defaultImageStream.readAllBytes();
					return ResponseEntity.ok()
							.header("Content-Type", "image/jpeg")
							.header("Content-Disposition", "inline; filename=\"dashboard-avatar.jpg\"")
							.body(defaultImageBytes);
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
				}
			} catch (IOException e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
			}
		}
	}

	// 上傳圖片
	@PostMapping("/{adminId}/uploadImage")
	public ResponseEntity<String> uploadProfileImage(@PathVariable Integer adminId,
			@RequestParam("profileImg") MultipartFile file) {
		Optional<AdminVO> admin = adminService.getUserById(adminId);
		if (admin.isPresent()) {
			try {
				if (file.isEmpty()) {
					return ResponseEntity.badRequest().body("上傳的檔案為空");
				}
				byte[] imageBytes = file.getBytes();
				AdminVO existingAdmin = admin.get();
				existingAdmin.setProfileImg(imageBytes);

				adminService.saveAdmin(existingAdmin);
				return ResponseEntity.ok("圖片上傳成功");
			} catch (IOException e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("圖片上傳失敗");
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("管理員資料未找到");
		}
	}
	
	// 新增管理員
	@PostMapping("/add")
	public ResponseEntity<?> addUser(@RequestBody AdminVO admin) {
	    if (admin.getAdminId() != null) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("新增失敗：管理員 ID 應為空");
	    }
	    
	    try {
	        AdminVO savedAdmin = adminService.saveAdmin(admin);
	        return ResponseEntity.status(HttpStatus.CREATED).body(savedAdmin);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("新增管理員失敗：" + e.getMessage());
	    }
	}
}