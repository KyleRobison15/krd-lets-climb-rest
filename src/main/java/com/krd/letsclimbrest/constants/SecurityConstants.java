package com.krd.letsclimbrest.constants;

public class SecurityConstants {

    public static final String[] WHITE_LIST_URL_ARRAY = {
            "/test",
            "/api/v1/auth/**",
            "/api/v1/grades",
            "/api/v1/boulder-grades",
            "/api/v1/styles",
            "/api/v1/dangers",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**"
    };

}
