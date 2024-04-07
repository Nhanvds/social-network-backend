package com.project.socialnetwork.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.socialnetwork.entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    private LocalDate createdTime;

    private LocalDate updatedTime;


    private UserCard user;

    private Set<PostImage> postImages;


}
