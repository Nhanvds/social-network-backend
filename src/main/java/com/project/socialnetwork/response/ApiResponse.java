package com.project.socialnetwork.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.socialnetwork.enums.ErrorCode;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T>{
    private boolean success;
    private int code;
    private String message;
    private T data;
    // Success
    public ApiResponse(String message, T result){
        this.success=true;
        this.message=message;
        this.data=result;
    }
    public ApiResponse(String message){
        this.success=true;
        this.message=message;
    }
    //Error
    public ApiResponse(ErrorCode errorCode){
        this.success=false;
        this.code=errorCode.getCode();
        this.message=errorCode.getMessage();
    }
    public ApiResponse(Boolean success,String message){
        this.success=success;
        this.message=message;
    }


}
