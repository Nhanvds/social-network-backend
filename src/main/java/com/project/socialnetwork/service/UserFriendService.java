package com.project.socialnetwork.service;

import com.project.socialnetwork.entity.UserFriend;
import com.project.socialnetwork.exception.ParserTokenException;
import com.project.socialnetwork.response.ListFriendResponse;
import org.springframework.data.domain.Pageable;

public interface UserFriendService {

    UserFriend addUserFriend(Long userFriendId,String token) throws ParserTokenException;
    UserFriend acceptFriend(Long userFriendId);
    void deleteUserFriend(Long userFriendId, String token) throws ParserTokenException;
}
