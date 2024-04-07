package com.project.socialnetwork.service;

import com.project.socialnetwork.components.JwtUtils;
import com.project.socialnetwork.entity.Post;
import com.project.socialnetwork.entity.PostUserStatus;
import com.project.socialnetwork.entity.User;
import com.project.socialnetwork.repository.PostUserStatusRepository;
import com.project.socialnetwork.repository.UserFriendRepository;
import com.project.socialnetwork.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostUserStatusServiceImpl implements PostUserStatusService{
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final UserFriendRepository userFriendRepository;
    private final PostUserStatusRepository postUserStatusRepository;
    @Override
    public void createPostUserStatus(String token, Post post) {
        try{
            Long userId = jwtUtils.getUserId(token);
            List<Long> userFriendIds = userFriendRepository.getAllUserFriendIds(userId);
            List<PostUserStatus> postUserStatuses = new ArrayList<>();
            List<User> userFriends = userRepository.getUsersByIds(userFriendIds);
            PostUserStatus postUserStatus=new PostUserStatus();
            for(User user:userFriends){
                postUserStatus.setPost(post);
                postUserStatus.setUser(user);
                postUserStatus.setHasSeen(false);
                postUserStatuses.add(postUserStatus);
            }
            postUserStatusRepository.saveAll(postUserStatuses);
        }catch (ParseException e){
            throw new RuntimeException(e);
        }
    }
}
