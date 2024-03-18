package com.rhmnarief.security.handler.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    private ResponseStatus status;
    private T data;

    public ApiResponse(T data, ResponseStatus status) {
        this.status = status;
        this.data = data;
    }
}
