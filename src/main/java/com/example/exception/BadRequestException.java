package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST ,reason = "Bad Request Http")
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super("Bad request: " + message);
    }
}
