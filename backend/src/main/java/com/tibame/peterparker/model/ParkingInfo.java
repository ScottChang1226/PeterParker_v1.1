package com.peterparker.model;

import javax.persistence.*;

@Entity
@Table(name = "ParkingInfo")  // 指定表名
public class ParkingInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer parkingId;

    @Column(nullable = false)
    private Integer capacity;

    @Column(length = 50)
    private String parkingName;

    @Column(length = 20)
    private String parkingRegion;

    @Column(length = 20)
    private String parkingLocation;

    @Column(nullable = false)
    private Double parkingLong;

    @Column(nullable = false)
    private Double parkingLat;

    @Column(nullable = false)
    private Integer holidayHourlyRate;

    @Column(nullable = false)
    private Integer workdayHourlyRate;

    @Lob
    private byte[] parkingImg; // 使用 byte[] 存储 BLOB 数据

    @Column(nullable = false)
    private Integer ownerNo;

    // Getters and Setters

    public Integer getParkingId() {
        return parkingId;
    }

    public void setParkingId(Integer parkingId) {
        this.parkingId = parkingId;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public String getParkingRegion() {
        return parkingRegion;
    }

    public void setParkingRegion(String parkingRegion) {
        this.parkingRegion = parkingRegion;
    }

    public String getParkingLocation() {
        return parkingLocation;
    }

    public void setParkingLocation(String parkingLocation) {
        this.parkingLocation = parkingLocation;
    }

    public Double getParkingLong() {
        return parkingLong;
    }

    public void setParkingLong(Double parkingLong) {
        this.parkingLong = parkingLong;
    }

    public Double getParkingLat() {
        return parkingLat;
    }

    public void setParkingLat(Double parkingLat) {
        this.parkingLat = parkingLat;
    }

    public Integer getHolidayHourlyRate() {
        return holidayHourlyRate;
    }

    public void setHolidayHourlyRate(Integer holidayHourlyRate) {
        this.holidayHourlyRate = holidayHourlyRate;
    }

    public Integer getWorkdayHourlyRate() {
        return workdayHourlyRate;
    }

    public void setWorkdayHourlyRate(Integer workdayHourlyRate) {
        this.workdayHourlyRate = workdayHourlyRate;
    }

    public byte[] getParkingImg() {
        return parkingImg;
    }

    public void setParkingImg(byte[] parkingImg) {
        this.parkingImg = parkingImg;
    }

    public Integer getOwnerNo() {
        return ownerNo;
    }

    public void setOwnerNo(Integer ownerNo) {
        this.ownerNo = ownerNo;
    }
}
