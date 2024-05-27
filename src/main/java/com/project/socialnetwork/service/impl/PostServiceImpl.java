package com.project.socialnetwork.service.impl;

import com.project.socialnetwork.components.JwtUtils;
import com.project.socialnetwork.dto.PostDto;
import com.project.socialnetwork.dto.filter.PageFilterDto;
import com.project.socialnetwork.dto.filter.PostFilterDto;
import com.project.socialnetwork.entity.*;
import com.project.socialnetwork.exception.ParserTokenException;
import com.project.socialnetwork.mapper.Mapper;
import com.project.socialnetwork.repository.*;
import com.project.socialnetwork.repository.custom.PostFilterRepository;
import com.project.socialnetwork.response.ListPostResponse;
import com.project.socialnetwork.response.PostDetailResponse;
import com.project.socialnetwork.response.PostResponse;
import com.project.socialnetwork.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostPrivacyStatusRepository postPrivacyStatusRepository;
    private final PostImageRepository postImageRepository;
    private final PostFilterRepository postFilterRepository;
    private final JwtUtils jwtUtils;


    @Override
    public PostDetailResponse getPostDetailResponse(Long portId) {
        Post post = postRepository.getPostById(portId)
                .orElseThrow(() -> new RuntimeException("Post này không còn tồn tại!"));

        return PostDetailResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .createdTime(post.getCreatedTime())
                .user(Mapper.mapToUserCard(post.getUser()))
                .postImages(post.getPostImages())
                .postPrivacyStatus(post.getPostPrivacyStatus())
                .build();
    }

    @Override
    public PostDetailResponse createPost(PostDto postDTO, String token) throws ParserTokenException {

        Long userId = jwtUtils.getUserId(token);
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại!"));

        PostPrivacyStatus postPrivacyStatus = postPrivacyStatusRepository
                .getPostPrivacyStatusById(postDTO.getPostPrivacyStatusId())
                .orElseThrow(() -> new RuntimeException("Quyền riêng tư không hợp lệ!"));

        Post post = Post.builder()
                .content(postDTO.getContent())
                .user(user)
                .postPrivacyStatus(postPrivacyStatus)
                .isLocked(false)
                .build();
        Post addedPost = postRepository.save(post);
        if (postDTO.getUrlPostImages() != null) {
            List<PostImage> postImages = new ArrayList<>();
            postDTO.getUrlPostImages().forEach(url ->
                    postImages.add(PostImage.builder()
                            .urlImage(url)
                            .post(addedPost)
                            .build()));

            List<PostImage> addedPostImages = postImageRepository.saveAll(postImages);
            addedPost.setPostImages(addedPostImages.stream().collect(Collectors.toSet()));
        }


        return PostDetailResponse.builder()
                .id(addedPost.getId())
                .content(addedPost.getContent())
                .postPrivacyStatus(addedPost.getPostPrivacyStatus())
                .createdTime(addedPost.getCreatedTime())
                .updatedTime(addedPost.getUpdatedTime())
                .user(Mapper.mapToUserCard(addedPost.getUser()))
                .postImages(addedPost.getPostImages())
                .build();

    }

    /**
     * Chỉ cho phép cập nhật tiêu đề hoặc chế độ chia sẻ bài viết
     *
     * @return
     */
    @Override
    public void updatePost(Long postId, PostDto postDto) {
        Post existedPost = postRepository.getPostById(postId)
                .orElseThrow(() -> new RuntimeException("Bài viết không ồn tại"));
        PostPrivacyStatus postPrivacyStatus = postPrivacyStatusRepository
                .getPostPrivacyStatusByName(postDto.getPostPrivacyName())
                .orElseThrow(() -> new RuntimeException("Chế độ bài viết không hợp lệ"));
        existedPost.setContent(postDto.getContent());
        existedPost.setPostPrivacyStatus(postPrivacyStatus);
        postRepository.save(existedPost);

    }

    @Override
    public void deletePost(Long id, String token) throws ParserTokenException {

        Long userId = jwtUtils.getUserId(token);
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại!"));
        Post post = postRepository.getPostById(id)
                .orElseThrow(() -> new RuntimeException("Post không tồn tại!"));
        if (post.getUser().getId() != user.getId()) {
            throw new RuntimeException("Không thể xóa bài viết!");
        }
        post.setIsLocked(true);
        postRepository.save(post);

    }

    @Override
    public void lockPost(Long id,Boolean isLocked) {
        Post post = postRepository.getPostById(id)
                .orElseThrow(() -> new RuntimeException("Post không tồn tại!"));
        post.setIsLocked(isLocked);
        postRepository.save(post);
    }

    // ADMIN
    @Override
    public PageImpl<PostResponse> searchPost(PageFilterDto<PostFilterDto> input, String token) throws ParserTokenException {
        long myId = jwtUtils.getUserId(token);
        Pageable pageable=input.getPageable();
        return postFilterRepository.searchPost(input, myId, pageable);

    }

    @Override
    public PageImpl<PostResponse> getPostsInHome(Integer page, Integer limit, Boolean asc, String common, Boolean hasLiked, String token) throws ParserTokenException {
        Long myId = jwtUtils.getUserId(token);
        Pageable pageable;
        if (page == null || limit == null || page < 0 || limit < 1) {
            pageable = Pageable.unpaged();
        } else {
            pageable = PageRequest.of(page, limit);
        }
        return postFilterRepository.getPostsInHome(pageable, myId, asc, common, hasLiked);
    }

    @Override
    public PageImpl<PostResponse> getPostsByUserId(Integer page, Integer limit,
                                                   Long userId, Boolean asc, String token, String common, Boolean hasLiked)
            throws ParserTokenException {
        Long myId = jwtUtils.getUserId(token);
        Pageable pageable;
        if (page == null || limit == null || page < 0 || limit < 1) {
            pageable = Pageable.unpaged();
        } else {
            pageable = PageRequest.of(page, limit);
        }
        return postFilterRepository.getPostsByUserId(pageable, myId, userId, asc, common, hasLiked);


    }


}
