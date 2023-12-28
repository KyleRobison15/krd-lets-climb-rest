package com.krd.letsclimbrest.controllers;

import com.krd.letsclimbrest.dto.AuthenticationRequest;
import com.krd.letsclimbrest.dto.AuthenticationResponse;
import com.krd.letsclimbrest.dto.ErrorResponse;
import com.krd.letsclimbrest.dto.RegisterRequest;
import com.krd.letsclimbrest.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Validated
@RequestMapping("/api/v1/auth")
@Tag(name = "1. Auth", description = "REST Endpoints for registering and authenticating users. Authentication is implemented using JSON Web Tokens.")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The new user was successfully registered.",
                    content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "BAD_REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest registerRequest){
        AuthenticationResponse authResponse = authService.register(registerRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }


    @PostMapping("/authenticate")
    @Operation(summary = "Authenticate a user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The user was successfully authenticated",
                    content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Invalid credentials. The user was not authenticated.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authRequest) {
        AuthenticationResponse authResponse = authService.authenticate(authRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

}
