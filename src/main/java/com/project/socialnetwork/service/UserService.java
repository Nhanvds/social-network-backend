package com.project.socialnetwork.service;

import com.nimbusds.jose.JOSEException;
import com.project.socialnetwork.dto.UserDto;
import com.project.socialnetwork.dto.filter.PageFilterDto;
import com.project.socialnetwork.dto.filter.UserFilerDto;
import com.project.socialnetwork.exception.InvalidCredentialsException;
import com.project.socialnetwork.exception.ParserTokenException;
import com.project.socialnetwork.response.Token;
import com.project.socialnetwork.response.UserCard;
import com.project.socialnetwork.response.UserDetailResponse;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.util.List;

public interface UserService {

    UserDetailResponse createUser(UserDto userDTO) throws InvalidCredentialsException;
    Token login(UserDto userDTO) throws InvalidCredentialsException, JOSEException, ParseException;
    boolean verifyUser(String verificationToken, Long userId) throws ParseException, InvalidCredentialsException, ParserTokenException;
    boolean sendEmail(String email) throws  InvalidCredentialsException;
    UserDetailResponse getUserDetailByToken(String token ) throws ParseException, InvalidCredentialsException, ParserTokenException;

    PageImpl<UserCard> searchUser(PageFilterDto<UserFilerDto> input,String token) throws ParserTokenException;

    void sendFriendRequest(String token, Long userFriendId) throws ParserTokenException, InvalidCredentialsException;

    void acceptFriendRequest(Long friendRequestId) throws InvalidCredentialsException;
}
