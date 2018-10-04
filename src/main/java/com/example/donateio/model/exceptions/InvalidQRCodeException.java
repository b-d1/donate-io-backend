package com.example.donateio.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNAUTHORIZED)
public class InvalidQRCodeException extends RuntimeException {

    public InvalidQRCodeException(String message) {
        super(message);
    }

}
