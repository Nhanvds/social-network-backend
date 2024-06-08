package com.project.socialnetwork.service.impl;

import com.project.socialnetwork.components.JwtUtils;
import com.project.socialnetwork.dto.PostCommentDto;
import com.project.socialnetwork.entity.Post;
import com.project.socialnetwork.entity.PostComment;
import com.project.socialnetwork.entity.User;
import com.project.socialnetwork.exception.ParserTokenException;
import com.project.socialnetwork.mapper.Mapper;
import com.project.socialnetwork.repository.PostCommentRepository;
import com.project.socialnetwork.repository.PostRepository;
import com.project.socialnetwork.repository.UserRepository;
import com.project.socialnetwork.repository.custom.CommentRepository;
import com.project.socialnetwork.response.PostCommentResponse;
import com.project.socialnetwork.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostCommentServiceImpl implements PostCommentService {
    private final PostCommentRepository postCommentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final JwtUtils jwtUtils;

    @Override
    public PageImpl<PostCommentResponse> getPostComments(Long postId, Pageable pageable) {
        return commentRepository.getCommentsByPostId(postId,pageable);

    }

    @Override
    public PostCommentResponse createPostComment(PostCommentDto postCommentDTO, String token) throws ParserTokenException {

            Long userId = jwtUtils.getUserId(token);
            User user = userRepository.getUserById(userId)
                    .orElseThrow(()->new RuntimeException("Người dùng không tồn tại!"));
            Post post = postRepository.getPostById(postCommentDTO.getPostId())
                    .orElseThrow(()->new RuntimeException("Bài viết không tồn tại!"));
            PostComment postComment = PostComment.builder()
                    .content(postCommentDTO.getContent())
                    .user(user)
                    .post(post)
                    .build();
            PostComment addedPostComment = postCommentRepository.save(postComment);
            return PostCommentResponse.builder()
                    .content(addedPostComment.getContent())
                    .user(Mapper.mapToUserCard(addedPostComment.getUser()))
                    .createdTime(addedPostComment.getCreatedAt())
                    .build();

    }

    @Override
    public void deletePostComment(Long id, String token) throws ParserTokenException {
            Long userId = jwtUtils.getUserId(token);
            User user = userRepository.getUserById(userId)
                    .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));
            PostComment postComment = postCommentRepository.getPostCommentById(id)
                    .orElseThrow(()->new RuntimeException("Comment này không còn tồn tại! "));
            if(postComment.getUser().getId()!=userId){
                throw new RuntimeException("Không thể xóa comment của người khác!");
            }
            postCommentRepository.deleteById(id);

    }
}
