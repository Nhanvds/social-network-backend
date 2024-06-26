package com.project.socialnetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private String content;

    private Long postPrivacyStatusId;

    private String postPrivacyName;

    private Set<String> urlPostImages;


}
