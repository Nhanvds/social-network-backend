package com.project.socialnetwork.controller;


import com.nimbusds.jose.JOSEException;
import com.project.socialnetwork.dto.UserDTO;
import com.project.socialnetwork.exception.InvalidCredentialsException;
import com.project.socialnetwork.response.ListFriendResponse;
import com.project.socialnetwork.response.MessageResponse;
import com.project.socialnetwork.service.UserFriendService;
import com.project.socialnetwork.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@CrossOrigin("*")
@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final UserFriendService userFriendService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMail(@RequestParam("email")String emailTo,
                                      @RequestHeader("Authorization") String token) throws ParseException {
        token= token.substring(7);
        userService.sendEmail(emailTo,token);
        return ResponseEntity.ok().body("ok");
    }

    @PostMapping("")
    public ResponseEntity<?> login(@RequestBody @Valid UserDTO userDTO) throws InvalidCredentialsException, JOSEException, ParseException {
        return ResponseEntity.ok().body(userService.login(userDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDTO userDTO){

        return ResponseEntity.ok().body(userService.createUser(userDTO));
    }

    @PostMapping("/verification")
    public ResponseEntity<?> verifyUser(
            @RequestParam("verification-token")String verificationToke,
            @RequestHeader("Authorization") String token) throws ParseException {
        token= token.substring(7);
        String message = userService.verifyUser(verificationToke,token);
        return ResponseEntity.ok().body(
                MessageResponse.builder().message(message).build()
                );
    }

    /**
     * @param keywork : tìm kiếm theo tên hoặc email
     * @param hasAccept == true: danh sách bạn bè; ==false danh sách lời mời kết bạn
     *                Sắp xếp mặc định
     */
    @GetMapping("friends")
    public ResponseEntity<?> getListFriends(
            @RequestHeader("Authorization") String token,
            @RequestParam(name = "keywork", defaultValue = "") String keywork,
            @RequestParam(name = "hasAccept", defaultValue = "true") boolean hasAccept,
            @RequestParam(name="page", defaultValue = "0") int page,
            @RequestParam(name = "limit",defaultValue="30") int limit
    ){
        token=token.substring(7);
        PageRequest pageRequest = PageRequest.of(page,limit);
        ListFriendResponse listFriendResponse = userFriendService
                .getFriends(keywork,hasAccept,pageRequest,token);
        return ResponseEntity.ok().body(listFriendResponse);
    }
}
