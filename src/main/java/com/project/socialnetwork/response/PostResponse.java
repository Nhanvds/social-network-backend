package com.project.socialnetwork.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.socialnetwork.entity.PostImage;
import com.project.socialnetwork.entity.PostPrivacyStatus;
import com.project.socialnetwork.entity.PostReaction;
import com.project.socialnetwork.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private Long id;

    private String content;

    private PostPrivacyStatus postPrivacyStatus;

    private LocalDate createdTime;

    private LocalDate updatedTime;


    private UserCard user;

    private Set<PostImage> postImages;

    private Set<PostReaction> postReactions;
}
