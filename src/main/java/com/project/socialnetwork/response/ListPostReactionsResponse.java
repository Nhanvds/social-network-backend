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
public class ListPostReactionsResponse {

    private Long total;
    private List<PostReactionResponse> postReactionResponseList;
}
