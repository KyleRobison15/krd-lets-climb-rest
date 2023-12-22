package com.krd.letsclimbrest.services;

import com.krd.letsclimbrest.dto.ChangePasswordRequest;

public interface UserService {
    void changePassword(ChangePasswordRequest request);
}
