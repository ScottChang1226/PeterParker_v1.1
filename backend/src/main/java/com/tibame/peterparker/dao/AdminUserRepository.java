package com.tibame.peterparker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tibame.peterparker.entity.UserVO;

public interface AdminUserRepository extends JpaRepository<UserVO, Integer> {
    // 可以根據需要定義自訂的查詢方法，例如：根據帳號查詢
    UserVO findByUserAccount(String userAccount);
}
