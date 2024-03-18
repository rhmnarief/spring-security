package com.rhmnarief.security.handler.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseStatus {
    private int code = HttpStatus.OK.value();
    private String message = "OK";
}
