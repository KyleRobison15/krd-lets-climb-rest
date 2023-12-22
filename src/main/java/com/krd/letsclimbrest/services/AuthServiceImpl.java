package com.krd.letsclimbrest.services;

import com.krd.letsclimbrest.dto.AuthenticationRequest;
import com.krd.letsclimbrest.dto.AuthenticationResponse;
import com.krd.letsclimbrest.dto.RegisterRequest;
import com.krd.letsclimbrest.entities.User;
import com.krd.letsclimbrest.exception.UsernameEmailAlreadyExistsException;
import com.krd.letsclimbrest.repositories.UserRepository;
import com.krd.letsclimbrest.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepo;

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Override
    public AuthenticationResponse register(RegisterRequest registerRequest) {

        // Check the database for existing username or password and throw an exception if either are not unique
        checkUsernameEmailExists(registerRequest);

        // Build our User
        User newUser = new User();
        newUser.setFirstName(registerRequest.getFirstName());
        newUser.setLastName(registerRequest.getLastName());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setRole("user");
        newUser.setIsActive(true);
        newUser.setClimbs(new ArrayList<>());

        // Save the new user to the database
        userRepo.save(newUser);

        // Generate a new JWT for this user
        String generatedToken = jwtService.generateToken(newUser);

        return new AuthenticationResponse(generatedToken);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {

        // Authenticate the user based on the username and password from the request
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        // The username and password are correct for this user at this point, so get the user and send back a token
        User user = userRepo.findByUsername(authenticationRequest.getUsername());

        // Generate a new JWT for this user
        String generatedToken = jwtService.generateToken(user);

        return new AuthenticationResponse(generatedToken);
    }

    @Override
    public void checkUsernameEmailExists(RegisterRequest registerRequest) {

        String message = "USER_ALREADY_EXISTS";
        String username = userRepo.findByUsername(registerRequest.getUsername()).getUsername();
        String email = userRepo.findByEmail(registerRequest.getEmail()).getEmail();
        Map<String, String> exceptionDetails = new HashMap<>();

        // Check if there is already a user with the same username AND email
        if(username != null && email != null){
            exceptionDetails.put("username", "Username, " + registerRequest.getUsername() + " already exists.");
            exceptionDetails.put("email", "Email, " + registerRequest.getEmail() + " already exists.");
            throw new UsernameEmailAlreadyExistsException(message, exceptionDetails, registerRequest);
        } else if (username != null){
            exceptionDetails.put("username", "A user with this username already exists");
            throw new UsernameEmailAlreadyExistsException(message, exceptionDetails, registerRequest);
        } else if (email != null){
            exceptionDetails.put("email", "A user with this email already exists");
            throw new UsernameEmailAlreadyExistsException(message, exceptionDetails, registerRequest);
        }

    }
}
