package com.project.socialnetwork.controller;


import com.nimbusds.jose.JOSEException;
import com.project.socialnetwork.dto.UserDTO;
import com.project.socialnetwork.enums.ErrorCode;
import com.project.socialnetwork.exception.ParserTokenException;
import com.project.socialnetwork.response.SuccessCode;
import com.project.socialnetwork.exception.InvalidCredentialsException;
import com.project.socialnetwork.response.*;
import com.project.socialnetwork.service.UserFriendService;
import com.project.socialnetwork.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;


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
    public ResponseEntity<?> login(@RequestBody @Valid UserDTO userDTO) throws InvalidCredentialsException, JOSEException, ParseException {
        Token token = userService.login(userDTO);
        return ResponseEntity.ok().body(new ApiResponse<Token>(SuccessCode.LOGIN_SUCCESSFULLY, token));
    }

    @GetMapping("")
    public ResponseEntity<?> getUserDetailByToken(
            @RequestHeader("Authorization") String token
    ) throws  InvalidCredentialsException, ParserTokenException {
        UserDetailResponse userDetailResponse = userService.getUserDetailByToken(token);
        return ResponseEntity.ok().body(new ApiResponse<UserDetailResponse>(
                SuccessCode.SUCCESSFULLY, userDetailResponse
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDTO userDTO) throws InvalidCredentialsException {
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

    /**
     * @param keywork   : tìm kiếm theo tên hoặc email
     * @param hasAccept == true: danh sách bạn bè; ==false danh sách lời mời kết bạn
     *                  Sắp xếp mặc định
     */
    @GetMapping("friends")
    public ResponseEntity<?> getListFriends(
            @RequestHeader("Authorization") String token,
            @RequestParam(name = "keywork", defaultValue = "") String keywork,
            @RequestParam(name = "hasAccept", defaultValue = "true") boolean hasAccept,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "30") int limit
    ) throws ParserTokenException {
        token = token.substring(7);
        PageRequest pageRequest = PageRequest.of(page, limit);
        ListFriendResponse listFriendResponse = userFriendService
                .getFriends(keywork, hasAccept, pageRequest, token);
        return ResponseEntity.ok().body(new ApiResponse<ListFriendResponse>(
                SuccessCode.SUCCESSFULLY, listFriendResponse
        ));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUser(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page" ,defaultValue = "0") int page,
            @RequestParam(name= "limit", defaultValue = "30") int limit

    ){
        PageRequest pageRequest = PageRequest.of(page,limit);
        List<UserCard> userCardList = userService.findUser(keyword,pageRequest);
        return ResponseEntity.ok().body(new ApiResponse<List<UserCard>>("ok",userCardList));
    }

    @PostMapping("/friend")
    public ResponseEntity<?> sendFriendRequest(
            @RequestHeader("Authorization") String token,
            @RequestParam("userFriendId") Long userFriendId
    ) throws InvalidCredentialsException, ParserTokenException {
        userService.sendFriendRequest(token,userFriendId);
        return ResponseEntity.ok().body(new ApiResponse<>("Gửi lời mời kết bạn thành công"));
    }

    @PutMapping("/friend")
    public ResponseEntity<?> acceptFriendRequest(
            @RequestParam("friendRequestId") Long friendRequestId
    ) throws InvalidCredentialsException {
        userService.acceptFriendRequest(friendRequestId);
        return ResponseEntity.ok().body(new ApiResponse<>("Xác nhận lời mời kết bạn thành công"));
    }
}
