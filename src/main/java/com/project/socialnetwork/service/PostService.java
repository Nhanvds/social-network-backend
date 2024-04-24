package com.project.socialnetwork.service;

import com.project.socialnetwork.dto.PostDTO;
import com.project.socialnetwork.exception.ParserTokenException;
import com.project.socialnetwork.response.ListPostResponse;
import com.project.socialnetwork.response.PostDetailResponse;
import org.springframework.data.domain.Pageable;

public interface PostService {

    ListPostResponse getListPosts(String token, Pageable pageable);

    ListPostResponse getMyListPosts(String token,Pageable pageable);

    ListPostResponse getListPostsLiked(String token,Pageable pageable) throws ParserTokenException;

    PostDetailResponse getPostDetailResponse(Long portId);

    PostDetailResponse createPost(PostDTO postDTO,String token) throws ParserTokenException;

    PostDetailResponse updatePost(Long postId,String content,long postPrivacyStatusId );

    void deletePost(Long id, String token) throws ParserTokenException;
}
