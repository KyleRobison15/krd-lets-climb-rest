package com.krd.letsclimbrest.exception;

import java.util.Map;

public class SortQueryException extends CustomException{

    public SortQueryException(String message, Map<String, String> details, String resource){
        super(message, details, resource);
    }

}
