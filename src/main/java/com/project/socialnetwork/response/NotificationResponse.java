package com.project.socialnetwork.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class NotificationResponse {

    private Long id;
    private Long postId;
    private String content;
    private LocalDateTime sendedAt;
    private Boolean hasRead;

    public NotificationResponse() {
    }

    public NotificationResponse(Long id, Long postId, String content, LocalDateTime sendedAt, Boolean hasRead) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.sendedAt = sendedAt;
        this.hasRead = hasRead;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSendedAt() {
        return sendedAt;
    }

    public void setSendedAt(LocalDateTime sendedAt) {
        this.sendedAt = sendedAt;
    }

    public Boolean getHasRead() {
        return hasRead;
    }

    public void setHasRead(Boolean hasRead) {
        this.hasRead = hasRead;
    }
}
