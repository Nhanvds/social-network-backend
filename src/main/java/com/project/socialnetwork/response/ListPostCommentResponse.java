package com.project.socialnetwork.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListPostCommentResponse {

    private Long total;
    private List<PostCommentResponse> postCommentResponseList;

}
