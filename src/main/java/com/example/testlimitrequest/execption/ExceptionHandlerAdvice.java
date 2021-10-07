package com.example.testlimitrequest.execption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(LimitIpException.class)
    public ResponseEntity getErrorResponse(LimitIpException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }
}
