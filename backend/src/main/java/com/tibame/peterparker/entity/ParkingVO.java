package com.tibame.peterparker.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ParkingInfo")
public class ParkingVO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parkingId")
    private Integer parkingId;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "parkingName")
    private String parkingName;

    @Column(name = "parkingRegion")
    private String parkingRegion;

    @Column(name = "parkingLocation")
    private String parkingLocation;

    @Column(name = "holidayHourlyRate", nullable = false)
    private Integer holidayHourlyRate;

    @Column(name = "workdayHourlyRate", nullable = false)
    private Integer workdayHourlyRate;

    @Column(name = "parkingLat", nullable = false)
    private Double parkingLat;

    @Column(name = "parkingLong", nullable = false)
    private Double parkingLong;

    @Lob
    @Column(name="parkingImg", columnDefinition = "LONGBLOB")
    private byte[] parkingImg;


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

    public Double getParkingLat() {
        return parkingLat;
    }

    public void setParkingLat(Double parkingLat) {
        this.parkingLat = parkingLat;
    }

    public Double getParkingLong() {
        return parkingLong;
    }

    public void setParkingLong(Double parkingLong) {
        this.parkingLong = parkingLong;
    }

    public byte[] getParkingImg() {
        return parkingImg;
    }

    public void setParkingImg(byte[] parkingImg) {
        this.parkingImg = parkingImg;
    }
}
