package com.project.socialnetwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    @JsonProperty("content")
    private String content;

    @JsonProperty("privacy_id")
    private Long postPrivacyStatusId;

//    @JsonProperty("created_at")
//    private LocalDate createdTime;

//    @JsonProperty("updated_at")
//    private LocalDate updatedTime;

    @JsonProperty("url_post_images")
    private Set<String> urlPostImages;


}
