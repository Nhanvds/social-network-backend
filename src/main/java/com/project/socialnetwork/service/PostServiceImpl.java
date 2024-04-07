package com.project.socialnetwork.service;

import com.project.socialnetwork.components.JwtUtils;
import com.project.socialnetwork.dto.PostDTO;
import com.project.socialnetwork.entity.*;
import com.project.socialnetwork.mapper.Mapper;
import com.project.socialnetwork.repository.*;
import com.project.socialnetwork.response.ListPostReactionsResponse;
import com.project.socialnetwork.response.ListPostResponse;
import com.project.socialnetwork.response.PostDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
    private final PostUserStatusRepository postUserStatusRepository;
    private final PostRepository postRepository;
    private final PostReactionRepository postReactionRepository;
    private final PostReactionServiceImpl postReactionService;
    private final UserRepository userRepository;
    private final PostPrivacyStatusRepository postPrivacyStatusRepository;
    private final PostImageRepository postImageRepository;
    private final JwtUtils jwtUtils;

    /**
     * @param token
     * @param pageable
     * @return danh sách bài viết của bạn bè mà chưa tương tác
     */
    @Override
    public ListPostResponse getListPosts(String token, Pageable pageable) {
        try {
            Long userId = jwtUtils.getUserId(token);
            String privacyPublic = PostPrivacyStatus.PRIVACY_PUBLIC;
            Page<Long> ids = postUserStatusRepository.getNewsFeedIdsByUserId(userId,privacyPublic,pageable);
            return convertToListPostResponse(ids);
        }catch (ParseException e){
            throw new RuntimeException(e);
        }
    }


    @Override
    public ListPostResponse getMyListPosts(String token, Pageable pageable) {
        try{
            Long userId = jwtUtils.getUserId(token);
            Page<Long> ids = postRepository.getMyPostIds(userId,pageable);
            return convertToListPostResponse(ids);
        }catch (ParseException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public ListPostResponse getListPostsLiked(String token, Pageable pageable) {
        try {
            Long userId = jwtUtils.getUserId(token);
            Page<Long> ids = postReactionRepository.getPostIdsLiked(userId,pageable);
            return convertToListPostResponse(ids);
        }catch (ParseException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public PostDetailResponse getPostDetailResponse(Long portId) {
        Post post = postRepository.getPostById(portId)
                .orElseThrow(()->new RuntimeException("Post này không còn tồn tại!"));

        return PostDetailResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .createdTime(post.getCreatedTime())
                .postPrivacyStatus(post.getPostPrivacyStatus())
                .build();
    }

    @Override
    public PostDetailResponse createPost(PostDTO postDTO, String token) {
        try{
            Long userId = jwtUtils.getUserId(token);
            User user = userRepository.getUserById(userId)
                    .orElseThrow(()->new RuntimeException("User không tồn tại!"));

            PostPrivacyStatus postPrivacyStatus = postPrivacyStatusRepository
                    .getPostPrivacyStatusById(postDTO.getPostPrivacyStatusId())
                    .orElseThrow(()->new RuntimeException("Quyền riêng tư không hợp lệ!"));

            Post post = Post.builder()
                    .content(postDTO.getContent())
                    .user(user)
                    .postPrivacyStatus(postPrivacyStatus)
                    .createdTime(postDTO.getCreatedTime())
                    .updatedTime(postDTO.getUpdatedTime())
                    .isLocked(false)
                    .build();
            Post addedPost = postRepository.save(post);
            List<PostImage> postImages = new ArrayList<>();
            postDTO.getUrlPostImages().forEach(url->
                    postImages.add(PostImage.builder()
                            .urlImage(url)
                                    .post(addedPost)
                            .build()));
            List<PostImage> addedPostImages = postImageRepository.saveAll(postImages);
            addedPost.setPostImages(addedPostImages.stream().collect(Collectors.toSet()));
            return PostDetailResponse.builder()
                    .id(addedPost.getId())
                    .content(addedPost.getContent())
                    .postPrivacyStatus(addedPost.getPostPrivacyStatus())
                    .createdTime(addedPost.getCreatedTime())
                    .updatedTime(addedPost.getUpdatedTime())
                    .user(Mapper.mapToUserCard(addedPost.getUser()))
                    .postImages(addedPost.getPostImages())
                    .build();
        }catch (ParseException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePost(Long id, String token) {
        try {
            Long userId = jwtUtils.getUserId(token);
            User user = userRepository.getUserById(userId)
                    .orElseThrow(() -> new RuntimeException("User không tồn tại!"));
            Post post = postRepository.getPostById(id)
                    .orElseThrow(()->new RuntimeException("Post không tồn tại!"));
            if(post.getUser().getId()!= user.getId()){
                throw new RuntimeException("Không thể xóa bài viết!");
            }
            postRepository.deleteById(id);
        }catch (ParseException e){
            throw new RuntimeException(e);
        }
    }


    /**
        @ids: danh sách id của các post
     */
    private ListPostResponse convertToListPostResponse(Page<Long> ids){
        List<Post> postList = postRepository.getPostsByIds(ids.toList());
        return ListPostResponse.builder()
                .total(ids.getTotalElements())
                .postResponseList(postList.stream().map(post->Mapper.mapToPostResponse(post)).toList())
                .build();
    }

}
