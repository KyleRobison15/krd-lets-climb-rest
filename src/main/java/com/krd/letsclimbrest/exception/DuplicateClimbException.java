package com.krd.letsclimbrest.exception;

import java.util.Map;

public class DuplicateClimbException extends CustomException{

    public DuplicateClimbException(String message, Map<String, String> details, String resource){
        super(message, details, resource);
    }

}
