package com.krd.letsclimbrest.services;

import com.krd.letsclimbrest.dto.ChangePasswordRequest;
import com.krd.letsclimbrest.entities.User;

public interface UserService {
    void changePassword(ChangePasswordRequest request);
    User getCurrentUser(String username);
}
