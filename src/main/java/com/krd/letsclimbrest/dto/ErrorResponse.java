package com.krd.letsclimbrest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

    @Schema(description = "Human-readable, server-defined error code representing the overall error class.")
    private String apiErrorCode;

    @Schema(description = "The description of the overall error.")
    private String message;

    @Schema(description = "A list of the actual individual errors")
    private List<Error> errorDetails;

}
