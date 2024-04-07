package com.project.socialnetwork.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandleException {

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<?> handleRuntimeException(RuntimeException e){
        return ResponseEntity.badRequest().body(MessageException.builder()
                .message(e.getMessage())
                .build());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        return ResponseEntity.badRequest()
                .body(MessageException.builder().message(e.getAllErrors().get(0).getDefaultMessage()).build());

    }
    @ExceptionHandler({InvalidCredentialsException.class})
    public ResponseEntity<?> handleInvalidCredentialsException(InvalidCredentialsException e){
        return ResponseEntity.badRequest()
                .body(MessageException.builder()
                        .message(e.getMessage()).build());
    }

}
