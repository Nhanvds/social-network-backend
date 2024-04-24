package com.project.socialnetwork.service;

import com.project.socialnetwork.entity.Post;
import com.project.socialnetwork.exception.ParserTokenException;

public interface PostUserStatusService {

    void createPostUserStatus(String token, Post Post) throws ParserTokenException;
}
