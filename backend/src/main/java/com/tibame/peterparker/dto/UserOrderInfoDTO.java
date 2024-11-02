package com.tibame.peterparker.dto;

import java.sql.Timestamp;

public class UserOrderInfoDTO {
    private Integer parkingId;
    private String parkingName;
    private Integer orderId;
    private Integer spaceId;
    private Integer userId;
    private String statusId;
    private Timestamp orderStartTime;
    private Timestamp orderEndTime;
    private Integer orderTotalIncome;
    private String userComment;
    private Timestamp orderModified;

    public Integer getParkingId() {
        return parkingId;
    }

    public void setParkingImg(Integer parkingId) {
        this.parkingId = parkingId;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public Timestamp getOrderStartTime() {
        return orderStartTime;
    }

    public void setOrderStartTime(Timestamp orderStartTime) {
        this.orderStartTime = orderStartTime;
    }

    public Timestamp getOrderEndTime() {
        return orderEndTime;
    }

    public void setOrderEndTime(Timestamp orderEndTime) {
        this.orderEndTime = orderEndTime;
    }

    public Integer getOrderTotalIncome() {
        return orderTotalIncome;
    }

    public void setOrderTotalIncome(Integer orderTotalIncome) {
        this.orderTotalIncome = orderTotalIncome;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public Timestamp getOrderModified() {
        return orderModified;
    }

    public void setOrderModified(Timestamp orderModified) {
        this.orderModified = orderModified;
    }

    public UserOrderInfoDTO(Integer parkingId, String parkingName, Integer orderId, Integer spaceId, Integer userId, String statusId, Timestamp orderStartTime, Timestamp orderEndTime, Integer orderTotalIncome, String userComment, Timestamp orderModified) {
        this.parkingId = parkingId;
        this.parkingName = parkingName;
        this.orderId = orderId;
        this.spaceId = spaceId;
        this.userId = userId;
        this.statusId = statusId;
        this.orderStartTime = orderStartTime;
        this.orderEndTime = orderEndTime;
        this.orderTotalIncome = orderTotalIncome;
        this.userComment = userComment;
        this.orderModified = orderModified;
    }

    public UserOrderInfoDTO() {}
}
