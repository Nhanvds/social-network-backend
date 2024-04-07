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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentResponse {
    private Long id;

    private String content;

    private UserCard user;

    private LocalDate createdTime;


}
