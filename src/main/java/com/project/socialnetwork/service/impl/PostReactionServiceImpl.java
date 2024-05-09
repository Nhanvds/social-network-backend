package com.project.socialnetwork.service.impl;

import com.project.socialnetwork.components.JwtUtils;
import com.project.socialnetwork.dto.PostReactionDto;
import com.project.socialnetwork.entity.Post;
import com.project.socialnetwork.entity.PostReaction;
import com.project.socialnetwork.entity.User;
import com.project.socialnetwork.exception.ParserTokenException;
import com.project.socialnetwork.mapper.Mapper;
import com.project.socialnetwork.repository.PostReactionRepository;
import com.project.socialnetwork.repository.PostRepository;
import com.project.socialnetwork.repository.UserRepository;
import com.project.socialnetwork.response.ListPostReactionsResponse;
import com.project.socialnetwork.response.PostReactionResponse;
import com.project.socialnetwork.service.PostReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostReactionServiceImpl implements PostReactionService {
    private final PostReactionRepository postReactionRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public ListPostReactionsResponse getReactionsByPostId(Long postId, Boolean hasLiked, Pageable pageable) {
        Page<Long> ids = postReactionRepository.getPostReactionIdsByPostId(
                postId, hasLiked, pageable
        );
        List<PostReaction> postReactions = postReactionRepository.getPagePostReaction(ids.toList());
        return ListPostReactionsResponse.builder()
                .total(ids.getTotalElements())
                .postReactionResponseList(postReactions.stream()
                        .map(postReaction -> Mapper.mapToPostReactionResponse(postReaction)).toList())
                .build();
    }

    @Override
    public void createPostReaction(PostReactionDto postReactionDTO, String token) throws ParserTokenException {

        Long userId = jwtUtils.getUserId(token);
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại!"));
        Post post = postRepository.getPostById(postReactionDTO.getPostId())
                .orElseThrow(() -> new RuntimeException("Post không tồn tại!"));
        Optional<PostReaction> existPostReaction = postReactionRepository.findIdByUserIdAndPostId(userId, post.getId());
        if(existPostReaction.isPresent() ){
            if(existPostReaction.get().getHasLiked()==postReactionDTO.getHasLiked()){
                postReactionRepository.deleteById(existPostReaction.get().getId());
            }else {
                postReactionRepository.save(PostReaction.builder()
                                .id(existPostReaction.get().getId())
                        .user(user)
                        .post(post)
                        .hasLiked(postReactionDTO.getHasLiked())
                        .build());
            }
        }else {
            postReactionRepository.save(PostReaction.builder()
                    .user(user)
                    .post(post)
                    .hasLiked(postReactionDTO.getHasLiked())
                    .build());

        }


    }

}
