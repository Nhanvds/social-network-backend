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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostUserStatusRepository postUserStatusRepository;
    private final PostUserStatusServiceImpl postUserStatusService;
    private final PostRepository postRepository;
    private final PostReactionRepository postReactionRepository;
    private final UserRepository userRepository;
    private final UserFriendRepository userFriendRepository;
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
        if(postDTO.getUrlPostImages()!=null){
            List<PostImage> postImages = new ArrayList<>();
            postDTO.getUrlPostImages().forEach(url ->
                    postImages.add(PostImage.builder()
                            .urlImage(url)
                            .post(addedPost)
                            .build()));

            List<PostImage> addedPostImages = postImageRepository.saveAll(postImages);
            addedPost.setPostImages(addedPostImages.stream().collect(Collectors.toSet()));
        }

        if(addedPost.getPostPrivacyStatus().getName().equals(PostPrivacyStatus.PRIVACY_PUBLIC)){
            postUserStatusService.createPostUserStatus(token,addedPost);
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
     * @return
     */
    @Override
    public PostDetailResponse updatePost(Long postId,PostDto postDto) {
        Post existedPost = postRepository.getPostById(postId)
                .orElseThrow(()-> new RuntimeException("Bài viết không ồn tại"));
        PostPrivacyStatus postPrivacyStatus = postPrivacyStatusRepository
                .getPostPrivacyStatusById(postDto.getPostPrivacyStatusId())
                .orElseThrow(()->new RuntimeException("Chế độ bài viết không hợp lệ"));
        existedPost.setContent(postDto.getContent());
        existedPost.setPostPrivacyStatus(postPrivacyStatus);

        Post updatedPost =  postRepository.save(existedPost);
        return PostDetailResponse.builder()
                .id(updatedPost.getId())
                .content(updatedPost.getContent())
                .postPrivacyStatus(updatedPost.getPostPrivacyStatus())
                .createdTime(updatedPost.getCreatedTime())
                .updatedTime(updatedPost.getUpdatedTime())
                .user(Mapper.mapToUserCard(updatedPost.getUser()))
                .postImages(updatedPost.getPostImages())
                .build();
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
            postRepository.deleteById(id);

    }

    @Override
    public PageImpl<PostResponse> searchPost(PageFilterDto<PostFilterDto> input, String token) throws ParserTokenException {
        long myId = jwtUtils.getUserId(token);
        Pageable pageable;
        if(input.getPageSize()==null||input.getPageNumber()==null|| input.getPageNumber()<0|| input.getPageSize()<1){
            pageable =Pageable.unpaged();
        }else {
            pageable = PageRequest.of(input.getPageNumber(), input.getPageSize());
        }
        return postFilterRepository.searchPost(input,myId,pageable);

    }



    /**
     * @ids: danh sách id của các post
     */
    private ListPostResponse convertToListPostResponse(Page<Long> ids) {
        List<Post> postList = postRepository.getPostsByIds(ids.toList());
        return ListPostResponse.builder()
                .total(ids.getTotalElements())
                .postResponseList(postList.stream().map(post -> Mapper.mapToPostResponse(post)).toList())
                .build();
    }

}
