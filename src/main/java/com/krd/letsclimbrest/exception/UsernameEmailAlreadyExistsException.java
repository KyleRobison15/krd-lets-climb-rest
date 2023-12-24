package com.krd.letsclimbrest.exception;

import java.util.Map;

public class UsernameEmailAlreadyExistsException extends CustomException {

    public UsernameEmailAlreadyExistsException(String message, Map<String, String> details, String path){
        super(message, details, path);
    }

}
