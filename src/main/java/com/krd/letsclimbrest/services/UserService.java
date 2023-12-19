package com.krd.letsclimbrest.services;

import com.krd.letsclimbrest.entities.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers ();
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    void deleteUserById(int id);
}
