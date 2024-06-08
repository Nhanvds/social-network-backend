package com.project.socialnetwork.service;

import com.project.socialnetwork.dto.PostReactionDto;
import com.project.socialnetwork.exception.ParserTokenException;
import org.springframework.data.domain.Pageable;

public interface PostReactionService {
    void createPostReaction(PostReactionDto postReactionDTO, String token) throws ParserTokenException;
}
