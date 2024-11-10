package com.tibame.peterparker.service;

import com.tibame.peterparker.entity.UserVO;
import com.tibame.peterparker.dao.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdminUserService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    // 新增或更新用戶
    public UserVO saveUser(UserVO user) {
        System.out.println(user);
        return adminUserRepository.save(user); // 如果有 ID，則是更新操作；如果無 ID，則是新增操作
    }

    // 根據 ID 查詢用戶
    public Optional<UserVO> getUserById(Integer userId) {
        return adminUserRepository.findById(userId);
    }

    // 刪除用戶
    @Transactional
    public void deleteUserById(Integer userId) {
        adminUserRepository.deleteById(userId); // 刪除 user 紀錄
    }

    // 查詢所有用戶
    public List<UserVO> getAllUsers() {
        return adminUserRepository.findAll();
    }

    public int countUsers() {
        int count = (int) adminUserRepository.count();
        System.out.println("User Count: " + count);
        return count;
    }
}
