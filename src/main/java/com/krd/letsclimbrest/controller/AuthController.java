package com.krd.letsclimbrest.controller;

import com.krd.letsclimbrest.dto.AuthRequestDto;
import com.krd.letsclimbrest.dto.AuthResponseDto;
import com.krd.letsclimbrest.entities.User;
import com.krd.letsclimbrest.security.JwtProvider;
import com.krd.letsclimbrest.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    JwtProvider jwtProvider;

    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(authRequestDto.getUsername(), authRequestDto.getPassword());
        Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);

        // Once the authentication manager handles our authentication request, we need to store the auth details somewhere
        // That's where the SecurityContextHolder comes in
        SecurityContextHolder.getContext().setAuthentication(authenticationResponse);
        String token = jwtProvider.generateToken(authenticationRequest);
        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);

    }

    @PostMapping("/register")
    public User register(@RequestBody User user){

        return authService.register(user);
    }

}
