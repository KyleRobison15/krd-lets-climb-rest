package com.krd.letsclimbrest.controllers;

import com.krd.letsclimbrest.dto.ErrorResponse;
import com.krd.letsclimbrest.entities.Climb;
import com.krd.letsclimbrest.services.ClimbService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/climbs")
public class ClimbController {

    @Autowired
    private ClimbService climbSvc;

    @GetMapping
    @Operation(summary = "Get a list of climbs for an authenticated user")
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Success"
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Bad Request",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
    })
    public ResponseEntity<List<Climb>> getClimbs(){

        // Get the current signed-in user from our database
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Get the climbs for this user from our database
        List<Climb> userClimbs = climbSvc.getClimbsByUserUsername(currentUser.getUsername());

        return ResponseEntity.ok(userClimbs);

    }



}
