package com.project.socialnetwork.service;

import com.project.socialnetwork.dto.PostCommentDto;
import com.project.socialnetwork.exception.ParserTokenException;
import com.project.socialnetwork.response.ListPostCommentResponse;
import com.project.socialnetwork.response.PostCommentResponse;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface PostCommentService {

    PageImpl<PostCommentResponse> getPostComments(Long postId, Pageable pageable);
    PostCommentResponse createPostComment(PostCommentDto postCommentDTO, String token) throws ParserTokenException;
    void deletePostComment(Long id,String token) throws ParserTokenException;
}
