package com.krd.letsclimbrest.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CustomException extends RuntimeException{

    private Map<String, String> details;
    private String path;

    public CustomException(String message, Map<String, String> details, String path){
        super(message);
        this.details = details;
        this.path = path;
    }

}
