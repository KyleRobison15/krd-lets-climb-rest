package com.krd.letsclimbrest.controllers;

import com.krd.letsclimbrest.dto.ErrorResponse;
import com.krd.letsclimbrest.entities.User;
import com.krd.letsclimbrest.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
@Tag(name = "2. User", description = "REST Endpoint for getting the current authenticated user.")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/current-user")
    @Operation(summary = "Get the current authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    public User getCurrentUser(){
        UserDetails currentUserDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.getCurrentUser(currentUserDetails.getUsername());
    }

}
