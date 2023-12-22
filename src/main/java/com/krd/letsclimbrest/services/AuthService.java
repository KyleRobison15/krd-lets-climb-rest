package com.krd.letsclimbrest.services;

import com.krd.letsclimbrest.dto.AuthenticationRequest;
import com.krd.letsclimbrest.dto.AuthenticationResponse;
import com.krd.letsclimbrest.dto.RegisterRequest;

public interface AuthService {
    AuthenticationResponse register(RegisterRequest registerRequest);
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
    void checkUsernameEmailExists(RegisterRequest registerRequest);
}
