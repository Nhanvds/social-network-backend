package com.project.socialnetwork.dto;


public class NotificationDto {
    private Long postId;
    private Long receiverId;
    private String content;

    public NotificationDto() {
    }

    public NotificationDto(Long postId, Long receiverId, String content) {
        this.postId = postId;
        this.receiverId = receiverId;
        this.content = content;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
