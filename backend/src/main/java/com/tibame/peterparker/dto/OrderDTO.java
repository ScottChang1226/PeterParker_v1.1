package com.tibame.peterparker.dto;

import java.sql.Timestamp;

public class OrderDTO {

    private Integer orderId;          // 訂單編號
    private Integer userId;           // 用戶編號
    private Integer parkingId;        // 停車場編號 (新增的屬性)
    private Integer spaceId;          // 車位編號
    private String statusId;          // 訂單狀態
    private String userComment;       // 用戶評價
    private Timestamp orderStartTime; // 訂單開始時間
    private Timestamp orderEndTime;   // 訂單結束時間
    private Timestamp orderModified;  // 訂單修改時間
    private Integer orderTotalIncome; // 訂單總金額

    // Constructors
    public OrderDTO() {}

    public OrderDTO(Integer orderId, Integer userId, Integer parkingId, Integer spaceId, String statusId, String userComment,
                    Timestamp orderStartTime, Timestamp orderEndTime, Timestamp orderModified, Integer orderTotalIncome) {
        this.orderId = orderId;
        this.userId = userId;
        this.parkingId = parkingId; // 新增
        this.spaceId = spaceId;
        this.statusId = statusId;
        this.userComment = userComment;
        this.orderStartTime = orderStartTime;
        this.orderEndTime = orderEndTime;
        this.orderModified = orderModified;
        this.orderTotalIncome = orderTotalIncome;
    }

    // Getters and Setters

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getParkingId() {
        return parkingId;
    }

    public void setParkingId(Integer parkingId) {
        this.parkingId = parkingId;
    }

    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
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

    public Timestamp getOrderModified() {
        return orderModified;
    }

    public void setOrderModified(Timestamp orderModified) {
        this.orderModified = orderModified;
    }

    public Integer getOrderTotalIncome() {
        return orderTotalIncome;
    }

    public void setOrderTotalIncome(Integer orderTotalIncome) {
        this.orderTotalIncome = orderTotalIncome;
    }
}
