package com.project.socialnetwork.service;

import com.nimbusds.jose.JOSEException;
import com.project.socialnetwork.dto.UserDTO;
import com.project.socialnetwork.entity.User;
import com.project.socialnetwork.exception.InvalidCredentialsException;
import com.project.socialnetwork.response.Token;

import java.text.ParseException;

public interface UserService {

    User createUser(UserDTO userDTO);
    Token login(UserDTO userDTO) throws InvalidCredentialsException, JOSEException, ParseException;
    String verifyUser(String verificationToken,String token) throws ParseException;
    void sendEmail(String email,String token) throws ParseException;
}
