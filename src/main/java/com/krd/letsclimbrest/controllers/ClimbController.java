package com.krd.letsclimbrest.controllers;

import com.krd.letsclimbrest.dto.ErrorResponse;
import com.krd.letsclimbrest.entities.Climb;
import com.krd.letsclimbrest.services.ClimbService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Validated
public class ClimbController {

    @Autowired
    private ClimbService climbSvc;

    @GetMapping("/climbs")
    @Operation(summary = "Get a list of climbs for an authenticated user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Climb.class)))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "BAD_REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    public ResponseEntity<List<Climb>> getClimbs() {

        // Get the current signed-in user from our database
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Get the climbs for this user from our database
        List<Climb> userClimbs = climbSvc.getClimbsByUserUsername(currentUser.getUsername());

        return ResponseEntity.ok(userClimbs);

    }

    @GetMapping("/climbs/{id}")
    @Operation(summary = "Get a climb by id.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(schema = @Schema(implementation = Climb.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "BAD_REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    public ResponseEntity<Climb> getClimbById(@Valid @NotNull @PathVariable Integer id) {

        // Get the current signed-in user from our database
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Get the climbs for this user from our database
        Climb climb = climbSvc.getClimbById(id, currentUser.getUsername());

        return ResponseEntity.ok(climb);

    }

    @DeleteMapping("/climbs/{id}")
    @Operation(summary = "Delete a climb by id.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "The climb was successfully deleted."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "BAD_REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    public ResponseEntity deleteClimbById(@Valid @NotNull @PathVariable Integer id) {

        // Get the current signed-in user from our database
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Get the climbs for this user from our database
        climbSvc.deleteClimbById(id, currentUser.getUsername());

        return new ResponseEntity(HttpStatus.NO_CONTENT);

    }

    @PostMapping("/climbs")
    @Operation(summary = "Create a new climb for a user.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "The climb was successfully created.",
                    content = @Content(schema = @Schema(implementation = Climb.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "BAD_REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    public ResponseEntity<Object> createClimb(@Valid @RequestBody Climb climb) {

        // Get the current signed-in user from our database
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Climb createdClimb = climbSvc.createClimbForUser(climb, currentUser.getUsername());

        return new ResponseEntity<>(createdClimb, HttpStatus.CREATED);

    }


}
