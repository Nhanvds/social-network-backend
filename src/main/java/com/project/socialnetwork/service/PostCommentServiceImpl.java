package com.project.socialnetwork.service;

import com.project.socialnetwork.components.JwtUtils;
import com.project.socialnetwork.dto.PostCommentDTO;
import com.project.socialnetwork.entity.Post;
import com.project.socialnetwork.entity.PostComment;
import com.project.socialnetwork.entity.User;
import com.project.socialnetwork.mapper.Mapper;
import com.project.socialnetwork.repository.PostCommentRepository;
import com.project.socialnetwork.repository.PostRepository;
import com.project.socialnetwork.repository.UserRepository;
import com.project.socialnetwork.response.ListPostCommentResponse;
import com.project.socialnetwork.response.PostCommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostCommentServiceImpl implements PostCommentService{
    private final PostCommentRepository postCommentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final JwtUtils jwtUtils;

    @Override
    public ListPostCommentResponse getPostComments(Long postId, Pageable pageable) {
        Page<Long> ids = postCommentRepository.getPostCommentIdsByPostId(postId,pageable);
        List<PostComment> postCommentResponseList = postCommentRepository.getPagePostComment(ids.toList());
        return ListPostCommentResponse.builder()
                .total(ids.getTotalElements())
                .postCommentResponseList(
                        postCommentResponseList.stream().map(postComment ->
                                Mapper.mapToPostCommentResponse(postComment)).toList()
                )
                .build();
    }

    @Override
    public PostCommentResponse createPostComment(PostCommentDTO postCommentDTO, String token) {
        try{
            Long userId = jwtUtils.getUserId(token);
            User user = userRepository.getUserById(userId)
                    .orElseThrow(()->new RuntimeException("Người dùng không tồn tại!"));
            Post post = postRepository.getPostById(postCommentDTO.getPostId())
                    .orElseThrow(()->new RuntimeException("Bài viết không tồn tại!"));
            PostComment postComment = PostComment.builder()
                    .content(postCommentDTO.getContent())
                    .user(user)
                    .post(post)
                    .createdAt(postCommentDTO.getCreatedAt())
                    .build();
            PostComment addedPostComment = postCommentRepository.save(postComment);
            return PostCommentResponse.builder()
                    .content(addedPostComment.getContent())
                    .user(Mapper.mapToUserCard(addedPostComment.getUser()))
                    .build();
        }catch (ParseException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePostComment(Long id, String token) {
        try {
            Long userId = jwtUtils.getUserId(token);
            User user = userRepository.getUserById(userId)
                    .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));
            PostComment postComment = postCommentRepository.getPostCommentById(id)
                    .orElseThrow(()->new RuntimeException("Comment này không còn tồn tại! "));
            if(postComment.getUser().getId()!=userId){
                throw new RuntimeException("Không thể xóa comment của người khác!");
            }
            postCommentRepository.deleteById(id);
        }catch (ParseException e){
            throw new RuntimeException(e);
        }
    }
}
