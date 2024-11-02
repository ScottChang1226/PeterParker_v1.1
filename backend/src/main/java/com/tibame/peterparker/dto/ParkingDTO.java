package com.tibame.peterparker.dto;

public class ParkingDTO {
    private Integer parkingId;
    private String parkingName;
    private String parkingType;
    private String parkingLocation;
    private Double latitude;
    private Double longitude;
    private Integer capacity;
    private Integer holidayHourlyRate;
    private Integer workdayHourlyRate;
    private String imageUrl;
    private Double rating;

    // Getters and Setters
    public Integer getParkingId() {
        return parkingId;
    }

    public void setParkingId(Integer parkingId) {
        this.parkingId = parkingId;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public String getParkingType() {
        return parkingType;
    }

    public void setParkingType(String parkingType) {
        this.parkingType = parkingType;
    }

    public String getParkingLocation() {
        return parkingLocation;
    }

    public void setParkingLocation(String parkingLocation) {
        this.parkingLocation = parkingLocation;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
