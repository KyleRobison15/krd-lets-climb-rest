package com.krd.letsclimbrest.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Handle configuration of the OpenAPI service documentation
 */
@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Kyle Robison",
                        email = "kylerobison@gmail.com",
                        url = "https://github.com/KyleRobison15/krd-lets-climb-rest.git"
                ),
                title = "krd-lets-climb-rest",
                description = "REST API Documentation for the Let's Climb full-stack web application",
                version = "1.0"
        ),
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "This API is secured using JSON Web Tokens.  " +
                "To make a request to a secured endpoint, you must obtain a token by authenticating as an existing user, or registering as a new user. " +
                "Then enter your token here to use the secured endpoints.",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

    /**
     * @return API documentation
     */
    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("com.krd.letsclimbrest")
                .pathsToMatch("/**")
                .build();
    }

}
