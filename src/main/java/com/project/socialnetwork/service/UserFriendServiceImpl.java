package com.project.socialnetwork.service;

import com.project.socialnetwork.components.JwtUtils;
import com.project.socialnetwork.entity.User;
import com.project.socialnetwork.entity.UserFriend;
import com.project.socialnetwork.enums.ErrorCode;
import com.project.socialnetwork.exception.ParserTokenException;
import com.project.socialnetwork.repository.UserFriendRepository;
import com.project.socialnetwork.repository.UserRepository;
import com.project.socialnetwork.response.ListFriendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserFriendServiceImpl implements UserFriendService{
    private final UserRepository userRepository;
    private final UserFriendRepository userFriendRepository;
    private final JwtUtils jwtUtils;

    @Override
    public UserFriend addUserFriend(Long userFriendId, String token) throws ParserTokenException {
       UserFriend userFriend = getUserFriend(userFriendId,token);
       UserFriend addedUserFriend = userFriendRepository.save(userFriend);
       return addedUserFriend;

    }

    @Override
    public UserFriend acceptFriend(Long userFriendId) {
        UserFriend userFriend = userFriendRepository.getUserFriendById(userFriendId)
                .orElseThrow(()->new RuntimeException("Lời mời kết bạn không tồn tại!"));
        userFriend.setHasAccepted(true);
        return userFriendRepository.save(userFriend);
    }

    @Override
    public void deleteUserFriend(Long userFriendId, String token) throws ParserTokenException {
       UserFriend userFriend = getUserFriend(userFriendId,token);
       userFriendRepository.delete(userFriend);
    }

    @Override
    public ListFriendResponse getFriends(String keyword,Boolean hasAccepted, Pageable pageable, String token) throws ParserTokenException {

            Long userId = jwtUtils.getUserId(token);
            Page<Long> friendIds = userFriendRepository.getUserFriendIds(userId,keyword,hasAccepted,pageable);
            List<UserFriend> friendList = userFriendRepository.getUserFriends(friendIds.toList());
            return ListFriendResponse.builder()
                    .total(friendIds.getTotalElements())
                    .friends(friendList)
                    .build();

    }



    private UserFriend getUserFriend(Long userFriendId, String token) throws ParserTokenException {

            Long myUserId = jwtUtils.getUserId(token);
            User myUser = userRepository.getUserById(myUserId)
                    .orElseThrow(()->new RuntimeException());
            User friendUser = userRepository.getUserById(userFriendId)
                    .orElseThrow(()->new RuntimeException());
            UserFriend userFriend = UserFriend.builder()
                    .firstUser(myUser)
                    .secondUser(friendUser)
                    .build();
            return userFriend;

    }


}
