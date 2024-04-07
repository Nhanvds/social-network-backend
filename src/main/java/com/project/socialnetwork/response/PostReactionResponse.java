package com.project.socialnetwork.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.socialnetwork.entity.Post;
import com.project.socialnetwork.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostReactionResponse {
    private Long id;

    private Boolean hasLiked;

    private UserCard user;
}
