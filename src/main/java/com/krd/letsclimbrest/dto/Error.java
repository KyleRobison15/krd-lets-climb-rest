package com.krd.letsclimbrest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Error {

    @Schema(description = "Human-readable, server-defined error code representing the specific error.")
    private String code;

    @Schema(description = "The part of the request that caused the error. Could be a field in the request body, path variable, or query param.")
    private String target;

    @Schema(description = "A description of the error.")
    private String description;

}
