package com.project.socialnetwork.service.impl;

import com.project.socialnetwork.components.JwtUtils;
import com.project.socialnetwork.entity.Post;
import com.project.socialnetwork.entity.PostUserStatus;
import com.project.socialnetwork.entity.User;
import com.project.socialnetwork.entity.UserFriend;
import com.project.socialnetwork.enums.ErrorCode;
import com.project.socialnetwork.exception.ParserTokenException;
import com.project.socialnetwork.repository.PostUserStatusRepository;
import com.project.socialnetwork.repository.UserFriendRepository;
import com.project.socialnetwork.repository.UserRepository;
import com.project.socialnetwork.service.PostUserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostUserStatusServiceImpl implements PostUserStatusService {
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final UserFriendRepository userFriendRepository;
    private final PostUserStatusRepository postUserStatusRepository;
    @Override
    public void createPostUserStatus(String token, Post post) throws ParserTokenException {

            Long userId = jwtUtils.getUserId(token);
            List<PostUserStatus> postUserStatuses = new ArrayList<>();
            List<Long> userFriendIds = userFriendRepository.getAllUserFriendIds(userId);
            List<UserFriend> userFriends = userFriendRepository.getUserFriends(userFriendIds);

            for(UserFriend userFriend:userFriends){
                PostUserStatus postUserStatus=new PostUserStatus();
                postUserStatus.setPost(post);
                if (userFriend.getFirstUser().getId() == userId) {
                    postUserStatus.setUser(userFriend.getSecondUser());
                } else {
                    postUserStatus.setUser(userFriend.getFirstUser());
                }
                postUserStatus.setHasSeen(false);
                postUserStatuses.add(postUserStatus);
            }
            postUserStatusRepository.saveAll(postUserStatuses);

    }
}
