package com.tibame.peterparker.service;

import com.tibame.peterparker.dao.ParkingRepository;
import com.tibame.peterparker.dto.FilterRequest;
import com.tibame.peterparker.dto.ParkingDTO;
import com.tibame.peterparker.entity.ParkingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParkingService {

    @Autowired
    private ParkingRepository parkingRepository;

    // 獲取所有停車場資訊，並轉換為 ParkingDTO
    public List<ParkingDTO> getAllParkingInfos() {
        List<ParkingVO> parkingInfos = parkingRepository.findAll();
        return parkingInfos.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 根據ID獲取停車場資訊
    public Optional<ParkingDTO> getParkingInfoById(Integer parkingId) {
        Optional<ParkingVO> parkingInfo = parkingRepository.findById(parkingId);
        return parkingInfo.map(this::convertToDTO);
    }

    // 保存或更新停車場資訊
    public ParkingVO saveOrUpdateParkingInfo(ParkingVO parkingInfo) {
        return parkingRepository.save(parkingInfo);
    }

    // 根據ID刪除停車場資訊
    public void deleteParkingInfoById(Integer parkingId) {
        parkingRepository.deleteById(parkingId);
    }

    // 根據經緯度查找附近的停車場，並轉換為 ParkingDTO
    public List<ParkingDTO> findNearbyParking(Double latitude, Double longitude) {
        // 實現查找附近停車場的邏輯（這裡可以根據實際邏輯篩選附近停車場）
        List<ParkingVO> parkingInfos = parkingRepository.findAll(); // TODO: 根據經緯度篩選
        return parkingInfos.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 根據關鍵字查找停車場，並轉換為 ParkingDTO
    public List<ParkingDTO> searchParkingByKeyword(String keyword) {
        // 根據關鍵字進行篩選
        List<ParkingVO> parkingInfos = parkingRepository.findAll(); // TODO: 根據關鍵字篩選
        return parkingInfos.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 獲取指定停車場的可用車位數量
    public Integer getAvailableSpaces(Integer parkingId) {
        // TODO: 實現獲取可用車位的邏輯
        return 0; // 佔位符
    }

    // 使用篩選條件查找符合的停車場，並轉換為 ParkingDTO
    public List<ParkingDTO> getFilteredParkingListings(FilterRequest filterRequest) {
        // 使用篩選條件查找符合的停車場
        List<ParkingVO> filteredParkingInfos = parkingRepository.findFilteredParkingListings(
                filterRequest.getTypes(),
                filterRequest.getDistance(),
                filterRequest.getLatitude(),
                filterRequest.getLongitude()
        );
        return filteredParkingInfos.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 將 ParkingVO 轉換為 ParkingDTO
    private ParkingDTO convertToDTO(ParkingVO parkingInfo) {
        ParkingDTO dto = new ParkingDTO();
        dto.setParkingId(parkingInfo.getParkingId());
        dto.setCapacity(parkingInfo.getCapacity());
        dto.setParkingName(parkingInfo.getParkingName());
        dto.setParkingLocation(parkingInfo.getParkingLocation());
        dto.setLongitude(parkingInfo.getParkingLong());
        dto.setLatitude(parkingInfo.getParkingLat());
        dto.setHolidayHourlyRate(parkingInfo.getHolidayHourlyRate());
        dto.setWorkdayHourlyRate(parkingInfo.getWorkdayHourlyRate());
        return dto;
    }
}
