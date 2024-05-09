package com.project.socialnetwork.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.socialnetwork.entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailResponse {
    private Long id;

    private String content;

    private PostPrivacyStatus postPrivacyStatus;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;


    private UserCard user;

    private Set<PostImage> postImages;


}
