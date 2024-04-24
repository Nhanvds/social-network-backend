package com.project.socialnetwork.enums;

import lombok.*;

@Getter
public enum ErrorCode {
    FAIL(1000,"Thất bại"),
    USER_NOT_EXISTED(1001,"User chưa tồn tại"),
    EMAIL_EXISTED(1002,"Email đã tồn tại"),
    USER_EXISTED(1003,"User đã tồn tại"),
    EMAIL_PASSWORD_WRONG(1004,"Email hoặc mật khẩu không đúng"),
    VERIFY_ACCOUNT_FAIL(1005,"Xác thực tài khoản thất bại"),
    ROLE_NOT_EXISTED(1006,"Role không tồn tại"),
    TOKEN_NOT_ACCEPTED(1007,"Token không hợp lệ"),
    INFO_REGISTER_FAILED(1008,"Thông tin đăng kí chưa hợp lệ!"),
    FRIEND_REQUEST_NOT_EXISTED(1009,"Lời mời kết bạn không tồn tại!")
    ;



    ErrorCode(int code,String message){
        this.code=code;
        this.message= message;
    }
    private int code;
    private String message;

}
