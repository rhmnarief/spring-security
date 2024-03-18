package com.rhmnarief.security.handler.response;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ApiPagedResponse<T> {
    private List<T> data;
    private ResponseStatus status;
    private ResponsePage page;
}
