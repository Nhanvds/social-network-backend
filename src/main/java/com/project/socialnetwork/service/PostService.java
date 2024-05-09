package com.project.socialnetwork.service;

import com.project.socialnetwork.dto.PostDto;
import com.project.socialnetwork.dto.filter.PageFilterDto;
import com.project.socialnetwork.dto.filter.PostFilterDto;
import com.project.socialnetwork.exception.ParserTokenException;
import com.project.socialnetwork.response.ListPostResponse;
import com.project.socialnetwork.response.PostDetailResponse;
import com.project.socialnetwork.response.PostResponse;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface PostService {


    PageImpl<PostResponse> searchPost(PageFilterDto<PostFilterDto> input, String token) throws ParserTokenException;

    PostDetailResponse getPostDetailResponse(Long portId);

    PostDetailResponse createPost(PostDto postDTO, String token) throws ParserTokenException;

    PostDetailResponse updatePost(Long postId,PostDto postDto );

    void deletePost(Long id, String token) throws ParserTokenException;
}
