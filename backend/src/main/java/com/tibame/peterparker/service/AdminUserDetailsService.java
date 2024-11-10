package com.tibame.peterparker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.tibame.peterparker.entity.AdminVO;
import com.tibame.peterparker.dao.AdminRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
// 根據用戶名從資料庫中加載用戶信息： 它從 AdminRepository 中查詢用戶，根據用戶名來查找相應的用戶記錄。
// 它將從資料庫中獲取的用戶信息轉換成 Spring Security 能夠理解的 UserDetails 對象，這樣 Spring Security 就能進行身份驗證和權限授權。
public class AdminUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    // 加載用戶詳細信息
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AdminVO> admin = adminRepository.findByAdminUsername(username);
        if (admin.isPresent()) {
            return new org.springframework.security.core.userdetails.User(
                    admin.get().getAdminUsername(), // 用戶名
                    admin.get().getAdminPassword(), // 密碼
                    new ArrayList<>()); // 這裡傳入空的權限列表，未使用具體權限
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
