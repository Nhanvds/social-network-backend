package com.project.socialnetwork.exception;

import com.project.socialnetwork.enums.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidCredentialsException extends Exception{
    private ErrorCode errorCode;
    public InvalidCredentialsException(ErrorCode errorCode){
        this.errorCode=errorCode;
    }
}
