package com.krd.letsclimbrest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Handle configuration of the OpenAPI service documentation
 */
@Configuration
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

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info().title("krd-lets-climb-rest API")
                        .contact(new Contact()
                                .name("Kyle Robison")
                                .url("https://github.com/KyleRobison15/krd-lets-climb-rest.git")
                                .email("kylerobison15@gmail.com"))
                        .description("REST API for the Let's Climb full-stack web application.")
                );
    }

}
