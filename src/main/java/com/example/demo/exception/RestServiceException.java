package com.example.demo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RestServiceException extends RuntimeException {
    private final HttpStatus status = HttpStatus.BAD_GATEWAY;
    public RestServiceException(String msg){
        super(msg);
    }
}
