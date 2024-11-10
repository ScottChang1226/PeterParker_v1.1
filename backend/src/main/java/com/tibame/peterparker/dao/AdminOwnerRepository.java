package com.tibame.peterparker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tibame.peterparker.entity.OwnerVO;

public interface AdminOwnerRepository extends JpaRepository<OwnerVO, Integer> {
    OwnerVO findByOwnerAccount(String ownerAccount);
}
