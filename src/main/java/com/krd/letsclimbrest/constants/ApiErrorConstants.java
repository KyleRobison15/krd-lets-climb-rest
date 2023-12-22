package com.krd.letsclimbrest.constants;

import com.krd.letsclimbrest.dto.ApiErrorCode;
import org.springframework.http.HttpStatus;

public class ApiErrorConstants {

    //////////////////////////// UNEXPECTED ERRORS //////////////////////////////////////
    public static final ApiErrorCode INTERNAL_SERVER_ERROR = ApiErrorCode
            .builder()
            .code("INTERNAL_SERVER_ERROR")
            .description("Something went wrong while processing this request. Please try again later.")
            .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
            .build();

    ///////////////////////// EXPECTED ERRORS ///////////////////////////////////////
    public static final ApiErrorCode AUTHENTICATION_FAILURE = ApiErrorCode
            .builder()
            .code("AUTHENTICATION_FAILURE")
            .description("API credentials are invalid or expired.")
            .httpStatus(HttpStatus.FORBIDDEN)
            .build();

    public static final ApiErrorCode VALIDATION_ERROR = ApiErrorCode
            .builder()
            .code("VALIDATION_ERROR")
            .description("One or more fields in the request are invalid.")
            .httpStatus(HttpStatus.BAD_REQUEST)
            .build();


    public static final ApiErrorCode MISSING_REQUIRED_PARAMETER = ApiErrorCode
            .builder()
            .code("MISSING_REQUIRED_PARAMETER")
            .description("A required field or parameter is missing.")
            .httpStatus(HttpStatus.BAD_REQUEST)
            .build();

    public static final ApiErrorCode MALFORMED_REQUEST_JSON = ApiErrorCode
            .builder()
            .code("MALFORMED_REQUEST_JSON")
            .description("The request JSON is not well formed.")
            .httpStatus(HttpStatus.BAD_REQUEST)
            .build();

    public static final ApiErrorCode INVALID_STRING_MIN_LENGTH = ApiErrorCode
            .builder()
            .code("INVALID_STRING_MIN_LENGTH")
            .description("The value of a field is too short.")
            .httpStatus(HttpStatus.BAD_REQUEST)
            .build();

    public static final ApiErrorCode INVALID_STRING_MAX_LENGTH = ApiErrorCode
            .builder()
            .code("INVALID_STRING_MAX_LENGTH")
            .description("The value of a field is too long.")
            .httpStatus(HttpStatus.BAD_REQUEST)
            .build();

    public static final ApiErrorCode INVALID_STRING_LENGTH = ApiErrorCode
            .builder()
            .code("INVALID_STRING_LENGTH")
            .description("The value of a field is either too short or too long.")
            .httpStatus(HttpStatus.BAD_REQUEST)
            .build();

    public static final ApiErrorCode INVALID_PARAMETER_TYPE = ApiErrorCode
            .builder()
            .code("INVALID_PARAMETER_TYPE")
            .description("The data type of a field does not conform to the expected data type.")
            .httpStatus(HttpStatus.BAD_REQUEST)
            .build();

    public static final ApiErrorCode INVALID_INTEGER_MIN_VALUE = ApiErrorCode
            .builder()
            .code("INVALID_INTEGER_MIN_VALUE")
            .description("The integer value of a field is too small.")
            .httpStatus(HttpStatus.BAD_REQUEST)
            .build();

    public static final ApiErrorCode INVALID_INTEGER_MAX_VALUE = ApiErrorCode
            .builder()
            .code("INVALID_INTEGER_MAX_VALUE")
            .description("The integer value of a field is too large.")
            .httpStatus(HttpStatus.BAD_REQUEST)
            .build();

    public static final ApiErrorCode INVALID_PARAMETER_VALUE = ApiErrorCode
            .builder()
            .code("INVALID_PARAMETER_VALUE")
            .description("The value of a field is invalid.")
            .httpStatus(HttpStatus.BAD_REQUEST)
            .build();

    public static final ApiErrorCode INVALID_ARRAY_MIN_ITEMS = ApiErrorCode
            .builder()
            .code("INVALID_ARRAY_MIN_ITEMS")
            .description("The number of items in an array parameter is too small.")
            .httpStatus(HttpStatus.BAD_REQUEST)
            .build();

    public static final ApiErrorCode INVALID_ARRAY_MAX_ITEMS = ApiErrorCode
            .builder()
            .code("INVALID_ARRAY_MAX_ITEMS")
            .description("The number of items in an array parameter is too large.")
            .httpStatus(HttpStatus.BAD_REQUEST)
            .build();

    public static final ApiErrorCode INVALID_ARRAY_LENGTH = ApiErrorCode
            .builder()
            .code("INVALID_ARRAY_LENGTH")
            .description("The number of items in an array parameter is too small or too large.")
            .httpStatus(HttpStatus.BAD_REQUEST)
            .build();


}
