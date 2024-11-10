package com.tibame.peterparker.service;

import com.tibame.peterparker.entity.AdminVO;
import com.tibame.peterparker.dao.AdminRepository;
import com.tibame.peterparker.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private JwtUtil jwtUtil;

	public String login(String username, String password) {
		Optional<AdminVO> adminOpt = adminRepository.findByAdminUsername(username);
		if (adminOpt.isPresent()) {
			AdminVO admin = adminOpt.get();
			if (password.equals(admin.getAdminPassword())) {
				return jwtUtil.generateToken(admin.getAdminUsername());
			} else {
				throw new RuntimeException("Invalid password");
			}
		} else {
			throw new RuntimeException("User not found");
		}
	}

	// 新增或更新管理員
	public AdminVO saveAdmin(AdminVO admin) {
		return adminRepository.save(admin);
	}

	// 根據 ID 查詢管理員
	public Optional<AdminVO> getUserById(Integer adminId) {
		return adminRepository.findById(adminId);
	}

	// 根據用戶名查詢
	public Optional<AdminVO> findByUsername(String username) {
		return adminRepository.findByAdminUsername(username);
	}

	// 刪除管理員
	public void deleteUserById(Integer adminId) {
		adminRepository.deleteById(adminId);
	}

	// 查詢所有管理員
	public List<AdminVO> getAllUsers() {
		return adminRepository.findAll();
	}
}
