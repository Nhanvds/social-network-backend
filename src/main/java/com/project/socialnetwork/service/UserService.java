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

    Token refreshToken(String token, String refreshToken) throws ParserTokenException, InvalidCredentialsException, JOSEException;
    boolean verifyUser(String verificationToken, Long userId) throws ParseException, InvalidCredentialsException, ParserTokenException;
    boolean sendEmail(String email) throws  InvalidCredentialsException;
    UserDetailResponse getUserDetailByToken(String token ) throws ParseException, InvalidCredentialsException, ParserTokenException;

    UserDetailResponse getUserDetailById(Long userId) throws InvalidCredentialsException;

    UserDetailResponse getUserDetailByEmail(String email) throws InvalidCredentialsException;
    PageImpl<UserDetailResponse> searchUser(PageFilterDto<UserFilerDto> input,String token) throws ParserTokenException;

    PageImpl<UserCard> getAllUsers(Integer page, Integer limit, String commonSearch, Boolean asc, String sortProperty);

    void updateUserByAdmin(UserDetailResponse user) throws InvalidCredentialsException;
    void updateUser(Long id,String token, UserDto user) throws InvalidCredentialsException, ParserTokenException;
    void updatePassword(String token, UserDto user) throws InvalidCredentialsException, ParserTokenException;
    void checkPassword(Long userId, String password) throws InvalidCredentialsException;
}
