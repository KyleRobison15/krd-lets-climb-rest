package com.krd.letsclimbrest.exception;

import java.util.Map;

public class InvalidFilterException extends CustomException{

    public InvalidFilterException(String message, Map<String, String> details, String resource){
        super(message, details, resource);
    }

}
