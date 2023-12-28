package com.krd.letsclimbrest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krd.letsclimbrest.constants.ApiErrorConstants;
import com.krd.letsclimbrest.dto.Error;
import com.krd.letsclimbrest.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is required for handling exceptions that occur BEFORE our business logic can handle them since requests are first being authenticated before hitting our API
 */
@Component
@RequiredArgsConstructor
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {

        String message = e.getMessage() + ". To fully authenticate, obtain a valid JSON Web Token (JWT) by logging in using the /api/v1/auth/authenticate endpoint. Then send the valid JWT in the Authorization header of each subsequent request to the secured endpoints.";

        List<Error> errorList = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse();

        errorList.add(
                Error.builder()
                        .code(ApiErrorConstants.AUTHENTICATION_FAILURE.getCode())
                        .target(request.getRequestURI())
                        .description(message)
                        .build()
        );

        errorResponse.setApiErrorCode(ApiErrorConstants.AUTHENTICATION_FAILURE.getCode());
        errorResponse.setMessage(ApiErrorConstants.AUTHENTICATION_FAILURE.getDescription());
        errorResponse.setErrorDetails(errorList);

        String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        try(OutputStream out = response.getOutputStream()){
            out.write(jsonErrorResponse.getBytes());
        }
    }
}
