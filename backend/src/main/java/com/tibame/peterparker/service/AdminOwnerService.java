package com.tibame.peterparker.service;

import com.tibame.peterparker.entity.OwnerVO;
import com.tibame.peterparker.dao.AdminOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class AdminOwnerService {

	@PersistenceContext
    private EntityManager entityManager;
	
    @Autowired
    private AdminOwnerRepository adminOwnerRepository;

    // 新增或更新業主
    public OwnerVO saveOwner(OwnerVO owner) {
        return adminOwnerRepository.save(owner);
    }

    // 根據 ID 查詢業主
    public Optional<OwnerVO> getOwnerById(Integer ownerNo) {
        return adminOwnerRepository.findById(ownerNo);
    }

    // 查詢所有業主
    public List<OwnerVO> getAllOwners() {
        return adminOwnerRepository.findAll();
    }

    // 刪除業主
    public void deleteOwnerById(Integer ownerNo) {
        adminOwnerRepository.deleteById(ownerNo);
    }
    
    public int countOwners() {
    	int count = (int) adminOwnerRepository.count();
        System.out.println("Owner Count: " + count);  // 加入這一行來確認資料庫的回應
        return (int) adminOwnerRepository.count();
    }
    
//    public List<OwnerWithParkingDTO> getAllOwnersWithParking() {
//        // 使用正確的實體名稱 OwnerVO 和 ParkingVO
//        String query = "SELECT new com.admin.dto.OwnerWithParkingDTO(o.ownerNo, o.ownerName, o.ownerAccount, o.ownerPassword, o.ownerPhone, p.parkingId) " +
//                       "FROM OwnerVO o LEFT JOIN ParkingVO p ON o.ownerNo = p.ownerNo";
//        return entityManager.createQuery(query, OwnerWithParkingDTO.class).getResultList();
//    }
//    
//    public Optional<OwnerWithParkingDTO> getOwnerWithParkingById(Integer ownerNo) {
//        // 實作查詢邏輯，假設使用 JPA
//        String query = "SELECT o FROM OwnerWithParkingDTO o LEFT JOIN ParkingVo p ON o.ownerNo = p.ownerNo WHERE o.ownerNo = :ownerNo";
//        List<OwnerWithParkingDTO> resultList = entityManager.createQuery(query, OwnerWithParkingDTO.class)
//                                                            .setParameter("ownerNo", ownerNo)
//                                                            .getResultList();
//        
//        // 檢查結果並返回 Optional
//        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
//    }
    
}
