package com.tibame.peterparker.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "orderInfo")
public class OrderVO implements Serializable {

    // Field
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderId")
    private Integer orderId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserVO user;

    // 直接與 Space 關聯
    @ManyToOne
    @JoinColumn(name = "spaceId", nullable = false)
    private Space space;

    @Column(name = "statusId", nullable = false, length = 20)
    private String statusId;

    @Column(name = "userComment", length = 255)
    private String userComment;

    @Column(name = "orderStartTime", nullable = false)
    private Timestamp orderStartTime;

    @Column(name = "orderEndTime")
    private Timestamp orderEndTime;

    @Column(name = "orderTotalIncome", nullable = false, columnDefinition = "int default 0")
    private Integer orderTotalIncome;

    @Column(name = "orderModified", nullable = false)
    @Version
    private Timestamp orderModified;



    // Constructors
    public OrderVO() {
    }

    public OrderVO(UserVO user, Space space, String statusId, String userComment, Timestamp orderStartTime, Timestamp orderEndTime, Integer orderTotalIncome, Timestamp orderModified) {
        this.user = user;
        this.space = space;
        this.statusId = statusId;
        this.userComment = userComment;
        this.orderStartTime = orderStartTime;
        this.orderEndTime = orderEndTime;
        this.orderTotalIncome = orderTotalIncome;
        this.orderModified = orderModified;
    }

    // Getters and Setters
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
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

    public Integer getOrderTotalIncome() {
        return orderTotalIncome;
    }

    public void setOrderTotalIncome(Integer orderTotalIncome) {
        this.orderTotalIncome = orderTotalIncome;
    }

    public Timestamp getOrderModified() {
        return orderModified;
    }

    public void setOrderModified(Timestamp orderModified) {
        this.orderModified = orderModified;
    }


}