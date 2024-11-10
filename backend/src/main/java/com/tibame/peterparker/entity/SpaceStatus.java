package com.tibame.peterparker.entity;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "space_status")
public class SpaceStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spaceReserveId")
    private Integer spaceReserveId;

    @Column(name = "orderId", nullable = false)
    private Integer orderId;

    @Column(name = "parkingId", nullable = false)
    private Integer parkingId;

    @Column(name = "spaceId", nullable = false)
    private Integer spaceId;

    @Column(name = "orderCreatedDate", nullable = false)  // 修改為訂單創建日期
    private Date orderCreatedDate;

    @Column(name = "orderStartTime", nullable = false)
    private Timestamp orderStartTime;

    @Column(name = "orderEndTime", nullable = false)
    private Timestamp orderEndTime;

    // Constructors
    public SpaceStatus() {}

    public SpaceStatus(Integer orderId, Integer parkingId, Integer spaceId, Date orderCreatedDate, Timestamp orderStartTime, Timestamp orderEndTime) {
        this.orderId = orderId;
        this.parkingId = parkingId;
        this.spaceId = spaceId;
        this.orderCreatedDate = orderCreatedDate;
        this.orderStartTime = orderStartTime;
        this.orderEndTime = orderEndTime;
    }

    // Getters and Setters
    public Integer getSpaceReserveId() {
        return spaceReserveId;
    }

    public void setSpaceReserveId(Integer spaceReserveId) {
        this.spaceReserveId = spaceReserveId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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

    public Date getOrderCreatedDate() {
        return orderCreatedDate;
    }

    public void setOrderCreatedDate(Date orderCreatedDate) {
        this.orderCreatedDate = orderCreatedDate;
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
}
