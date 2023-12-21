package com.krd.letsclimbrest.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiErrorCode {

    private String code;

    private String description;

    private Integer httpStatus;

}
