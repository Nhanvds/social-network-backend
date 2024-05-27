package com.project.socialnetwork.mapper;

import com.project.socialnetwork.dto.UserDto;
import com.project.socialnetwork.entity.*;
import com.project.socialnetwork.response.*;

import java.util.stream.Collectors;

public class Mapper {

    //map from DTO to Entity
    public static User mapToUser(UserDto userDTO) {
        return User.builder()
                .email(userDTO.getEmail())
                .userName(userDTO.getUsername())
                .password(userDTO.getPassword())
                .urlAvatar(userDTO.getUrlAvatar())
                .build();
    }



    // map from entity to response
    public static UserDetailResponse mapToUserDetailResponse(User user) {
        return UserDetailResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUserName())
                .urlAvatar(user.getUrlAvatar())
                .description(user.getDescription())
                .isLocked(user.getIsLocked())
                .roles(user.getRoles().stream().toList())
                .build();
    }

    public static UserCard mapToUserCard(User user) {
        return UserCard.builder()
                .id(user.getId())
                .username(user.getUserName())
                .urlAvatar(user.getUrlAvatar())
                .build();
    }

    public static PostResponse mapToPostResponse(Post post) {

        return PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .postPrivacyStatus(post.getPostPrivacyStatus().getName())
                .createdTime(post.getCreatedTime())
                .updatedTime(post.getUpdatedTime())
                .postImages(post.getPostImages().stream().map(image -> image.getUrlImage()).collect(Collectors.toList()))
                .userId(post.getUser().getId())
                .urlAvatar(post.getUser().getUrlAvatar())
                .username(post.getUser().getUserName())
                .likedReactions(post.getPostReactions().stream().filter(postReaction -> postReaction.getHasLiked() == true).collect(Collectors.toSet()).size())
                .dislikedReactions(post.getPostReactions().stream().filter(postReaction -> postReaction.getHasLiked() == false).collect(Collectors.toSet()).size())
                .build();
    }

    public static PostCommentResponse mapToPostCommentResponse(PostComment postComment) {

        return PostCommentResponse.builder()
                .id(postComment.getId())
                .content(postComment.getContent())
                .createdTime(postComment.getCreatedAt())
                .user(mapToUserCard(postComment.getUser()))
                .build();
    }

    public static PostReactionResponse mapToPostReactionResponse(PostReaction postReaction) {
        return PostReactionResponse.builder()
                .id(postReaction.getId())
                .user(mapToUserCard(postReaction.getUser()))
                .build();
    }





}
