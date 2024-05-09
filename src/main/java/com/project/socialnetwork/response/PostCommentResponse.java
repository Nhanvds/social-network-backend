package com.project.socialnetwork.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.socialnetwork.entity.Post;
import com.project.socialnetwork.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public class PostCommentResponse {
    private Long id;

    private String content;

    private UserCard user;

    private LocalDateTime createdTime;

    public PostCommentResponse(Long id, String content, UserCard user, LocalDateTime createdTime) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.createdTime = createdTime;
    }

    public PostCommentResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserCard getUser() {
        return user;
    }

    public void setUser(UserCard user) {
        this.user = user;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
