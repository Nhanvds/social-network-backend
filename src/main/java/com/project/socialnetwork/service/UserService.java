package com.project.socialnetwork.service;

import com.nimbusds.jose.JOSEException;
import com.project.socialnetwork.dto.UserDTO;
import com.project.socialnetwork.exception.InvalidCredentialsException;
import com.project.socialnetwork.exception.ParserTokenException;
import com.project.socialnetwork.response.ApiResponse;
import com.project.socialnetwork.response.Token;
import com.project.socialnetwork.response.UserCard;
import com.project.socialnetwork.response.UserDetailResponse;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.util.List;

public interface UserService {

    UserDetailResponse createUser(UserDTO userDTO) throws InvalidCredentialsException;
    Token login(UserDTO userDTO) throws InvalidCredentialsException, JOSEException, ParseException;
    boolean verifyUser(String verificationToken, Long userId) throws ParseException, InvalidCredentialsException, ParserTokenException;
    boolean sendEmail(String email) throws  InvalidCredentialsException;
    UserDetailResponse getUserDetailByToken(String token ) throws ParseException, InvalidCredentialsException, ParserTokenException;

    List<UserCard> findUser(String keyword, Pageable pageable);

    void sendFriendRequest(String token, Long userFriendId) throws ParserTokenException, InvalidCredentialsException;

    void acceptFriendRequest(Long friendRequestId) throws InvalidCredentialsException;
}
