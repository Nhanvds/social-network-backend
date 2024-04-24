package com.project.socialnetwork.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SuccessCode {
    public static String SUCCESSFULLY="Thành công";
    public static String VERIFY_ACCOUNT_SUCCESSFULLY="Xác thực tài khoản thành công";
    public static String CREATE_ACCOUNT_SUCCESSFULLY="Tạo tài khoản thành công";
    public static String LOGIN_SUCCESSFULLY="Đăng nhập thành công";
    public static String SEND_EMAIL_SUCCESSFULLY="Gửi mã xác thực thành công";


}
