package com.project.socialnetwork.service;

import com.project.socialnetwork.entity.Post;

public interface PostUserStatusService {

    void createPostUserStatus(String token, Post Post);
}
