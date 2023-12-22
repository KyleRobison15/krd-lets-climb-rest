package com.krd.letsclimbrest.exception;

import com.krd.letsclimbrest.dto.RegisterRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class UsernameEmailAlreadyExistsException extends RuntimeException {

    private Map<String, String> details;
    private RegisterRequest request;

    public UsernameEmailAlreadyExistsException(String message, Map<String, String> details, RegisterRequest request){
        super(message);
        this.details = details;
        this.request = request;
    }

}
