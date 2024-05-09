package com.project.socialnetwork.service;

import com.project.socialnetwork.dto.PostReactionDto;
import com.project.socialnetwork.exception.ParserTokenException;
import com.project.socialnetwork.response.ListPostReactionsResponse;
import com.project.socialnetwork.response.PostReactionResponse;
import org.springframework.data.domain.Pageable;

public interface PostReactionService {
    ListPostReactionsResponse getReactionsByPostId(Long postId, Boolean hasLiked, Pageable pageable);
    void createPostReaction(PostReactionDto postReactionDTO, String token) throws ParserTokenException;
}
