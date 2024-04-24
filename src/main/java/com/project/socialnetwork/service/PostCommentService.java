package com.project.socialnetwork.service;

import com.project.socialnetwork.dto.PostCommentDTO;
import com.project.socialnetwork.exception.ParserTokenException;
import com.project.socialnetwork.response.ListPostCommentResponse;
import com.project.socialnetwork.response.PostCommentResponse;
import org.springframework.data.domain.Pageable;

public interface PostCommentService {

    ListPostCommentResponse getPostComments(Long postId, Pageable pageable);
    PostCommentResponse createPostComment(PostCommentDTO postCommentDTO,String token) throws ParserTokenException;
    void deletePostComment(Long id,String token) throws ParserTokenException;
}
