package com.project.socialnetwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostReactionDTO {

    @JsonProperty("has_liked")
    private Boolean hasLiked;

    @JsonProperty("post_id")
    private Long postId;
}
