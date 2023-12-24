package com.krd.letsclimbrest.exception;

import java.util.Map;

public class NotFoundException extends CustomException{

    public NotFoundException(String message, Map<String, String> details, String resource){
        super(message, details, resource);
    }

}
