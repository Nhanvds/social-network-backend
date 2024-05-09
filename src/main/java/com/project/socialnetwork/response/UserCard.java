package com.project.socialnetwork.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
public class UserCard {
    private Long id;
    private String username;
    private String urlAvatar;

    public UserCard() {
    }

    public UserCard(Long id, String username, String urlAvatar) {
        this.id = id;
        this.username = username;
        this.urlAvatar = urlAvatar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
