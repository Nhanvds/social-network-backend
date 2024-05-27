package com.project.socialnetwork.controller;


import com.nimbusds.jose.JOSEException;
import com.project.socialnetwork.dto.UserDto;
import com.project.socialnetwork.dto.filter.PageFilterDto;
import com.project.socialnetwork.dto.filter.UserFilerDto;
import com.project.socialnetwork.enums.ErrorCode;
import com.project.socialnetwork.exception.ParserTokenException;
import com.project.socialnetwork.mapper.Mapper;
import com.project.socialnetwork.response.SuccessCode;
import com.project.socialnetwork.exception.InvalidCredentialsException;
import com.project.socialnetwork.response.*;
import com.project.socialnetwork.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Optional;


@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserServiceImpl userService;

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
    @GetMapping("/{userId}/detail")
    public ResponseEntity<?> getUserDetailById(
            @PathVariable("userId") Long userId
    ) throws InvalidCredentialsException{
        UserDetailResponse userDetailResponse = userService.getUserDetailById(userId);
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
               new ApiResponse< PageImpl<UserDetailResponse>>("ok",userService.searchUser(input,token)));
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(name = "page",defaultValue = "0")Integer page,
            @RequestParam(name = "limit",defaultValue = "10")Integer limit,
            @RequestParam(name = "common") String commonSearch,
            @RequestParam(name = "asc") Boolean asc,
            @RequestParam(name = "sortProperty") String sortProperty
    ){
        return ResponseEntity.ok()
                .body(new ApiResponse<PageImpl<UserCard>>("ok",userService.getAllUsers(page,limit,commonSearch,asc,sortProperty)));
    }

    @PostMapping("/{userId}/check/password")
    public ResponseEntity<?> checkPassword(
            @PathVariable Long userId,
            @RequestBody String password
    ) throws InvalidCredentialsException {
        userService.checkPassword(userId,password);
        return ResponseEntity.ok().body(new ApiResponse<>("ok"));
    }

    @PutMapping("")
    public ResponseEntity<?> updateUserByAdmin(
            @RequestBody UserDetailResponse user
    ) throws InvalidCredentialsException {
        userService.updateUserByAdmin(user);
        return ResponseEntity.ok()
                .body(new ApiResponse<>("ok"));
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<?> updateUser(
            @RequestBody UserDto userDto,
            @RequestHeader("Authorization")String token,
            @PathVariable Long userId
    ) throws InvalidCredentialsException, ParserTokenException {
        this.userService.updateUser(userId,token,userDto);
        return ResponseEntity.ok().body(new ApiResponse<>("ok"));
    }

    @GetMapping("/{email}/email")
    public ResponseEntity<?> getUserByEmail(
            @PathVariable("email") String email
    ) throws InvalidCredentialsException {
        return ResponseEntity.ok()
                .body(new ApiResponse<>("ok",userService.getUserDetailByEmail(email)));
    }
    @PutMapping("/{verify-token}/update-password")
    public ResponseEntity<?> updatePassword(
            @RequestBody UserDto userDto,
            @PathVariable("verify-token") String token
    ) throws InvalidCredentialsException, ParserTokenException {
        this.userService.updatePassword(token,userDto);
        return ResponseEntity.ok().body(new ApiResponse<>("ok"));
    }
}
