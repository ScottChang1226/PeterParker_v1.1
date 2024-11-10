package com.tibame.peterparker.dto;

public class UserCommentDTO {
    private String userComment;
    private Integer orderId;

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public UserCommentDTO(String userComment, Integer orderId) {
        this.userComment = userComment;
        this.orderId = orderId;
    }

    public UserCommentDTO() {}
}
