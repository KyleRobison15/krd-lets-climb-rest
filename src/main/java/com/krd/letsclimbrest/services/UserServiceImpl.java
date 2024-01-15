package com.krd.letsclimbrest.services;

import com.krd.letsclimbrest.dto.ChangePasswordRequest;
import com.krd.letsclimbrest.entities.User;
import com.krd.letsclimbrest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void changePassword(ChangePasswordRequest request) {

    }

    @Override
    public User getCurrentUser(String username) {
        return userRepository.findByUsername(username);
    }

}
