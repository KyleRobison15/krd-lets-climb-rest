package com.krd.letsclimbrest.controller;

import com.krd.letsclimbrest.dto.AuthenticationRequest;
import com.krd.letsclimbrest.dto.AuthenticationResponse;
import com.krd.letsclimbrest.dto.RegisterRequest;
import com.krd.letsclimbrest.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest){
        AuthenticationResponse authResponse = authService.register(registerRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authRequest) {
        AuthenticationResponse authResponse = authService.authenticate(authRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

}
