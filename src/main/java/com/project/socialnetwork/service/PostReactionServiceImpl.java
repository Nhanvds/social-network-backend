package com.project.socialnetwork.service;

import com.project.socialnetwork.components.JwtUtils;
import com.project.socialnetwork.dto.PostReactionDTO;
import com.project.socialnetwork.entity.Post;
import com.project.socialnetwork.entity.PostReaction;
import com.project.socialnetwork.entity.User;
import com.project.socialnetwork.mapper.Mapper;
import com.project.socialnetwork.repository.PostReactionRepository;
import com.project.socialnetwork.repository.PostRepository;
import com.project.socialnetwork.repository.UserRepository;
import com.project.socialnetwork.response.ListPostReactionsResponse;
import com.project.socialnetwork.response.PostReactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostReactionServiceImpl implements PostReactionService{
    private final PostReactionRepository postReactionRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    @Override
    public ListPostReactionsResponse getReactionsByPostId(Long postId, Boolean hasLiked, Pageable pageable) {
        Page<Long> ids = postReactionRepository.getPostReactionIdsByPostId(
                postId, hasLiked,pageable
                ) ;
        List<PostReaction> postReactions = postReactionRepository.getPagePostReaction(ids.toList());
        return ListPostReactionsResponse.builder()
                .total(ids.getTotalElements())
                .postReactionResponseList(postReactions.stream()
                        .map(postReaction -> Mapper.mapToPostReactionResponse(postReaction)).toList())
                .build();
    }

    @Override
    public PostReactionResponse createPostReaction(PostReactionDTO postReactionDTO, String token) {
        try{
        Long userId = jwtUtils.getUserId(token);
        User user = userRepository.getUserById(userId)
                .orElseThrow(()->new RuntimeException("User không tồn tại!"));
            Post post = postRepository.getPostById(postReactionDTO.getPostId())
                    .orElseThrow(()->new RuntimeException("Post không tồn tại!"));
        PostReaction postReaction = postReactionRepository.save(
                PostReaction.builder()
                        .post(post)
                        .user(user)
                        .hasLiked(postReactionDTO.getHasLiked())
                        .build());
        return PostReactionResponse.builder()
                .id(postReaction.getId())
                .user(Mapper.mapToUserCard(postReaction.getUser()))
                .hasLiked(postReaction.getHasLiked())
                .build();

        }catch (ParseException e){
            throw new RuntimeException(e);
        }


    }

    @Override
    public void deletePostReaction(Long postReactionId, String token) {
        try {


            Long userId = jwtUtils.getUserId(token);
            PostReaction postReaction = postReactionRepository.findById(postReactionId)
                    .orElseThrow(() -> new RuntimeException("Dislike lỗi"));
            if(postReaction.getUser().getId()!=userId){
                throw new RuntimeException("Dislike lỗi");
            }
            postReactionRepository.deleteById(postReaction.getId());
        }catch (ParseException e){
            throw new RuntimeException(e);
        }

    }
}
