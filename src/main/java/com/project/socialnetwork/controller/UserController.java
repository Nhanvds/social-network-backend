package com.project.socialnetwork.controller;


import com.nimbusds.jose.JOSEException;
import com.project.socialnetwork.dto.UserDto;
import com.project.socialnetwork.dto.filter.PageFilterDto;
import com.project.socialnetwork.dto.filter.UserFilerDto;
import com.project.socialnetwork.enums.ErrorCode;
import com.project.socialnetwork.exception.ParserTokenException;
import com.project.socialnetwork.response.SuccessCode;
import com.project.socialnetwork.exception.InvalidCredentialsException;
import com.project.socialnetwork.response.*;
import com.project.socialnetwork.service.UserFriendService;
import com.project.socialnetwork.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;


@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserServiceImpl userService;
    private final UserFriendService userFriendService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMail(@RequestParam("email") String emailTo) throws InvalidCredentialsException {

        userService.sendEmail(emailTo);
        return ResponseEntity.ok().body(new ApiResponse(SuccessCode.SEND_EMAIL_SUCCESSFULLY));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserDto userDTO) throws InvalidCredentialsException, JOSEException, ParseException {
        Token token = userService.login(userDTO);
        return ResponseEntity.ok().body(new ApiResponse<Token>(SuccessCode.LOGIN_SUCCESSFULLY, token));
    }

    @GetMapping("")
    public ResponseEntity<?> getUserDetailByToken(
            @RequestHeader("Authorization") String token
    ) throws InvalidCredentialsException, ParserTokenException {
        UserDetailResponse userDetailResponse = userService.getUserDetailByToken(token);
        return ResponseEntity.ok().body(new ApiResponse<UserDetailResponse>(
                SuccessCode.SUCCESSFULLY, userDetailResponse
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDto userDTO) throws InvalidCredentialsException {
        UserDetailResponse userDetailResponse = userService.createUser(userDTO);
        return ResponseEntity.ok().body(
                new ApiResponse<UserDetailResponse>(SuccessCode.CREATE_ACCOUNT_SUCCESSFULLY, userDetailResponse)
        );
    }

    @PostMapping("/verification")
    public ResponseEntity<?> verifyUser(
            @RequestParam("verification-token") String verificationToken,
            @RequestParam("id") Long id) throws InvalidCredentialsException {
        boolean hasSuccessed = userService.verifyUser(verificationToken, id);
        if (hasSuccessed) {
            return ResponseEntity.ok().body(
                    new ApiResponse(SuccessCode.VERIFY_ACCOUNT_SUCCESSFULLY)
            );
        } else {
            return ResponseEntity.ok().body(
                    new ApiResponse(ErrorCode.VERIFY_ACCOUNT_FAIL)
            );
        }
    }


    @PostMapping("/search")
    public ResponseEntity<?> searchUser(
            @RequestHeader("Authorization")String token,
            @RequestBody PageFilterDto<UserFilerDto> input
    ) throws ParserTokenException {
        return ResponseEntity.ok().body(
               new ApiResponse< PageImpl<UserCard>>("ok",userService.searchUser(input,token)));
    }

    @PostMapping("/friend")
    public ResponseEntity<?> sendFriendRequest(
            @RequestHeader("Authorization") String token,
            @RequestParam("userFriendId") Long userFriendId
    ) throws InvalidCredentialsException, ParserTokenException {
        userService.sendFriendRequest(token, userFriendId);
        return ResponseEntity.ok().body(new ApiResponse<>("Gửi lời mời kết bạn thành công"));
    }

    @PutMapping("/friend")
    public ResponseEntity<?> acceptFriendRequest(
            @RequestParam("friendRequestId") Long friendRequestId
    ) throws InvalidCredentialsException {
        userService.acceptFriendRequest(friendRequestId);
        return ResponseEntity.ok().body(new ApiResponse<>("Xác nhận lời mời kết bạn thành công"));
    }
    @DeleteMapping("/friend/{id}")
    public ResponseEntity<?> deleteFriendRequest(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String token
    ) throws ParserTokenException {
        userFriendService.deleteUserFriend(id,token);
        return ResponseEntity.ok()
                .body(new ApiResponse("Xóa lời mời kết bạn thành công"));
    }


}
