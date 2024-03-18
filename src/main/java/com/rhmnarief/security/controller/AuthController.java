package com.rhmnarief.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.rhmnarief.security.dto.request.AuthenticationRequest;
import com.rhmnarief.security.dto.request.RegisterRequest;
import com.rhmnarief.security.dto.response.AuthenticationResponse;
import com.rhmnarief.security.handler.response.CustomResponse;
import com.rhmnarief.security.service.AuthenticationService;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<CustomResponse<Object>> process(
            @Valid @RequestBody RegisterRequest request) {
        CustomResponse<Object> customResponse;
        try {
            customResponse = new CustomResponse<>(
                    "success",
                    "Update successfully",
                    service.register(request));
            return new ResponseEntity<>(customResponse, HttpStatus.CREATED);

        } catch (Exception e) {
            customResponse = new CustomResponse<>(
                    "failed",
                    e.getMessage().toString(),
                    null);
            return new ResponseEntity<>(customResponse, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, StreamWriteException, DatabindException, java.io.IOException {
        service.refreshToken(request, response);
    }

}
