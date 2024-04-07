package com.project.socialnetwork.service;

import com.project.socialnetwork.dto.PostReactionDTO;
import com.project.socialnetwork.entity.PostReaction;
import com.project.socialnetwork.response.ListPostReactionsResponse;
import com.project.socialnetwork.response.PostReactionResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface PostReactionService {
    ListPostReactionsResponse getReactionsByPostId(Long postId, Boolean hasLiked, Pageable pageable);
    PostReactionResponse createPostReaction(PostReactionDTO postReactionDTO, String token);
    void deletePostReaction(Long postreactionId, String token);
}
