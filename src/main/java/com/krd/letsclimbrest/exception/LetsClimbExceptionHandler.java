package com.krd.letsclimbrest.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.krd.letsclimbrest.constants.ApiErrorConstants;
import com.krd.letsclimbrest.dto.Error;
import com.krd.letsclimbrest.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<Error> errorList = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse();

        errorList.add(
                Error.builder()
                        .code(ex.getCause().getClass().getSimpleName())
                        .target(request.getDescription(false).substring(4))
                        .description(ex.getMessage())
                        .build()
        );

        errorResponse.setApiErrorCode(ApiErrorConstants.MALFORMED_REQUEST_JSON.getCode());
        errorResponse.setMessage(ApiErrorConstants.MALFORMED_REQUEST_JSON.getDescription());
        errorResponse.setErrorDetails(errorList);


        return new ResponseEntity<>(errorResponse, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        ex.printStackTrace();
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
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
                            .target(e.getPath() + "/" + target)
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
     * Handle the UsernameEmailAlreadyExistsException
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    ErrorResponse handleNotFoundException(NotFoundException e) {
        List<Error> errorList = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse();

        e.getDetails().forEach((target, message) -> {
            errorList.add(
                    Error.builder()
                            .code(e.getMessage())
                            .target(e.getPath() + "/" + target)
                            .description(message)
                            .build()
            );
        });

        errorResponse.setApiErrorCode(ApiErrorConstants.NOT_FOUND.getCode());
        errorResponse.setMessage(ApiErrorConstants.NOT_FOUND.getDescription());
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

    /**
     * Handle the UsernameEmailAlreadyExistsException
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JsonParseException.class)
    ErrorResponse handleJsonParseException(JsonParseException e) {

        List<Error> errorList = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse();

            errorList.add(
                    Error.builder()
                            .code(e.getClass().getSimpleName())
                            .target(e.getRequestPayloadAsString())
                            .description(e.getMessage())
                            .build()
            );

        errorResponse.setApiErrorCode(ApiErrorConstants.MALFORMED_REQUEST_JSON.getCode());
        errorResponse.setMessage(ApiErrorConstants.MALFORMED_REQUEST_JSON.getDescription());
        errorResponse.setErrorDetails(errorList);

        return errorResponse;

    }


}
