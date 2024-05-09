package com.project.socialnetwork.response;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Builder
public class PostResponse {

    private Long id;

    private String content;

    private String postPrivacyStatus;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private Long userId;

    private String username;

    private String urlAvatar;

    private List<String> postImages;

    private Integer likedReactions;

    private Integer dislikedReactions;

    private Boolean hasLiked;

    private Boolean hasDisLiked;

    public PostResponse() {
    }

    public PostResponse(Long id, String content, String postPrivacyStatus, LocalDateTime createdTime, LocalDateTime updatedTime, Long userId, String username, String urlAvatar, List<String> postImages, Integer likedReactions, Integer dislikedReactions, Boolean hasLiked, Boolean hasDisLiked) {
        this.id = id;
        this.content = content;
        this.postPrivacyStatus = postPrivacyStatus;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
        this.userId = userId;
        this.username = username;
        this.urlAvatar = urlAvatar;
        this.postImages = postImages;
        this.likedReactions = likedReactions;
        this.dislikedReactions = dislikedReactions;
        this.hasLiked = hasLiked;
        this.hasDisLiked = hasDisLiked;
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

    public String getPostPrivacyStatus() {
        return postPrivacyStatus;
    }

    public void setPostPrivacyStatus(String postPrivacyStatus) {
        this.postPrivacyStatus = postPrivacyStatus;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    public List<String> getPostImages() {
        return postImages;
    }

    public void setPostImages(List<String> postImages) {
        this.postImages = postImages;
    }

    public Integer getLikedReactions() {
        return likedReactions;
    }

    public void setLikedReactions(Integer likedReactions) {
        this.likedReactions = likedReactions;
    }

    public Integer getDislikedReactions() {
        return dislikedReactions;
    }

    public void setDislikedReactions(Integer dislikedReactions) {
        this.dislikedReactions = dislikedReactions;
    }

    public Boolean getHasLiked() {
        return hasLiked;
    }

    public void setHasLiked(Boolean hasLiked) {
        this.hasLiked = hasLiked;
    }

    public Boolean getHasDisLiked() {
        return hasDisLiked;
    }

    public void setHasDisLiked(Boolean hasDisLiked) {
        this.hasDisLiked = hasDisLiked;
    }
}
