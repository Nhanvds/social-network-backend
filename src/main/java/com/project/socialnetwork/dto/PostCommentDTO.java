package com.project.socialnetwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentDTO {

    private String content;

    @JsonProperty("created_at")
    private LocalDate createdAt;


    @JsonProperty("post_id")
    private Long postId;
}
