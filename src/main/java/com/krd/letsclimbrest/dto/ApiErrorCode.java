package com.krd.letsclimbrest.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiErrorCode {

    private String code;

    private String description;

    private Enum<HttpStatus> httpStatus;

}
