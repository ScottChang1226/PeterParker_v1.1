package com.tibame.peterparker.dao;

import com.tibame.peterparker.entity.AdminVO;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<AdminVO, Integer> {
    // 根據 adminUsername 查詢管理員
    Optional<AdminVO> findByAdminUsername(String admin_username);
}
