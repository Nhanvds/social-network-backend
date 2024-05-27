package com.project.socialnetwork.exception;


import com.project.socialnetwork.enums.ErrorCode;
import com.project.socialnetwork.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.text.ParseException;

@ControllerAdvice
public class HandleException {

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<?> handleRuntimeException(RuntimeException e){
        return ResponseEntity.ok().body(new ApiResponse(false,e.getMessage()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        return ResponseEntity.ok()
                .body(new ApiResponse(ErrorCode.INFO_REGISTER_FAILED));
    }
    @ExceptionHandler({InvalidCredentialsException.class})
    public ResponseEntity<?> handleInvalidCredentialsException(InvalidCredentialsException ex){
        return ResponseEntity.badRequest()
                .body(new ApiResponse(ex.getErrorCode()));
    }
    @ExceptionHandler({ParserTokenException.class})
    public ResponseEntity<?> handleParseException(ParserTokenException ex){
        return ResponseEntity.badRequest()
                .body(new ApiResponse(ErrorCode.TOKEN_NOT_ACCEPTED));
    }

    public ResponseEntity<?> handleDefaultHandlerExceptionResolver(DefaultHandlerExceptionResolver ex){
        return ResponseEntity.badRequest()
                .body(new ApiResponse(ErrorCode.FAIL));
    }


}
