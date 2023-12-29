package com.krd.letsclimbrest.exception;

import java.util.Map;

public class InvalidGradeException extends CustomException{

    public InvalidGradeException(String message, Map<String, String> details, String resource){
        super(message, details, resource);
    }

}
