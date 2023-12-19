package com.krd.letsclimbrest.services;

import com.krd.letsclimbrest.entities.User;
import com.krd.letsclimbrest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepo;

    @Override
    public User register(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsActive(true);
        user.setRole("user");
        user.setClimbs(new ArrayList<>()); // Add an empty array as the user's climbs when they register

        return userRepo.save(user);
    }
}
