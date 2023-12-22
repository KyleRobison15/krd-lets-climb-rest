package com.krd.letsclimbrest.exception;

import com.krd.letsclimbrest.constants.ApiErrorConstants;
import com.krd.letsclimbrest.dto.Error;
import com.krd.letsclimbrest.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class LetsClimbExceptionHandler extends ResponseEntityExceptionHandler {


    /**
     * Handle the MethodArugmentNotValidException
     * Because we are extending the ResponseEntityExceptionHandler class we can customize the error handling by overriding this method
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<Error> errorList = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorList.add(
                    Error.builder()
                            .code(error.getCode())
                            .target(ex.getBindingResult().getTarget().getClass().getSimpleName() + "/" + error.getField())
                            .description(error.getDefaultMessage())
                            .build()
            );
        });

        errorResponse.setApiErrorCode(ApiErrorConstants.VALIDATION_ERROR.getCode());
        errorResponse.setMessage(ApiErrorConstants.VALIDATION_ERROR.getDescription());
        errorResponse.setErrorDetails(errorList);

        return new ResponseEntity<>(errorResponse, headers, status);
    }

    /**
     * Handle the UsernameEmailAlreadyExistsException
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameEmailAlreadyExistsException.class)
    ErrorResponse handleUsernamePasswordAlreadyExistsException(UsernameEmailAlreadyExistsException e) {

        List<Error> errorList = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse();

        e.getDetails().forEach((target, message) -> {
            errorList.add(
                    Error.builder()
                            .code(e.getMessage())
                            .target(e.getRequest().getClass().getSimpleName() + "/" + target)
                            .description(message)
                            .build()
            );
        });

        errorResponse.setApiErrorCode(ApiErrorConstants.VALIDATION_ERROR.getCode());
        errorResponse.setMessage(ApiErrorConstants.VALIDATION_ERROR.getDescription());
        errorResponse.setErrorDetails(errorList);

        return errorResponse;

    }

    /**
     * Handle the BadCredentialsException
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(BadCredentialsException.class)
    ErrorResponse exceptionHandler(BadCredentialsException e) {
        log.debug("Failed authentication attempt.");

        List<Error> errorDetails = new ArrayList<>();

        errorDetails.add(Error.builder()
                .code(ApiErrorConstants.AUTHENTICATION_FAILURE.getCode())
                .target("/authenticate")
                .description(ApiErrorConstants.AUTHENTICATION_FAILURE.getDescription())
                .build()

        );

        return ErrorResponse.builder()
                .apiErrorCode(ApiErrorConstants.AUTHENTICATION_FAILURE.getCode())
                .message(ApiErrorConstants.AUTHENTICATION_FAILURE.getDescription())
                .errorDetails(errorDetails)
                .build();

    }


}
