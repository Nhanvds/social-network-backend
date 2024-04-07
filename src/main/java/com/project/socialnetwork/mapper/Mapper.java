package com.project.socialnetwork.mapper;

import com.project.socialnetwork.dto.PostCommentDTO;
import com.project.socialnetwork.dto.UserDTO;
import com.project.socialnetwork.entity.Post;
import com.project.socialnetwork.entity.PostComment;
import com.project.socialnetwork.entity.PostReaction;
import com.project.socialnetwork.entity.User;
import com.project.socialnetwork.response.PostCommentResponse;
import com.project.socialnetwork.response.PostReactionResponse;
import com.project.socialnetwork.response.PostResponse;
import com.project.socialnetwork.response.UserCard;

public class Mapper {

    //map from DTO to Entity
    public static User mapToUser(UserDTO userDTO){
        return User.builder()
                .email(userDTO.getEmail())
                .userName(userDTO.getUserName())
                .password(userDTO.getPassword())
                .build();
    }




    // map from entity to response
    public static UserCard mapToUserCard(User user){
        return UserCard.builder()
                .id(user.getId())
                .username(user.getUserName())
                .urlAvatar(user.getUrlAvatar())
                .build();
    }

    public static PostResponse mapToPostResponse(Post post){

        return PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .postPrivacyStatus(post.getPostPrivacyStatus())
                .createdTime(post.getCreatedTime())
                .updatedTime(post.getUpdatedTime())
                .postImages(post.getPostImages())
                .user(mapToUserCard(post.getUser()))
                .postReactions(post.getPostReactions())
                .build();
    }

    public static PostCommentResponse mapToPostCommentResponse(PostComment postComment){

        return PostCommentResponse.builder()
                .id(postComment.getId())
                .content(postComment.getContent())
                .createdTime(postComment.getCreatedAt())
                .user(mapToUserCard(postComment.getUser()))
                .build();
    }

    public static PostReactionResponse mapToPostReactionResponse(PostReaction postReaction){
        return PostReactionResponse.builder()
                .id(postReaction.getId())
                .user(mapToUserCard(postReaction.getUser()))
                .build();
    }


}