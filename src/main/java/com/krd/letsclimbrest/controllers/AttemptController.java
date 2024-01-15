package com.krd.letsclimbrest.controllers;

import com.krd.letsclimbrest.dto.ErrorResponse;
import com.krd.letsclimbrest.entities.Attempt;
import com.krd.letsclimbrest.entities.AttemptImage;
import com.krd.letsclimbrest.entities.Climb;
import com.krd.letsclimbrest.services.AttemptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/climbs/{climbId}")
@Validated
@Tag(name = "4. Attempts", description = "REST Endpoints for creating, updating and deleting Attempts and Attempt Images for an authenticated user's climbs.")
public class AttemptController {

    @Autowired
    AttemptService attemptSvc;

    @PostMapping("/attempts")
    @Operation(summary = "Create a new attempt for a user climb.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "The attempt was successfully created and added to the climb.",
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
    public ResponseEntity<Object> createAttemptForUserClimb(@PathVariable(name = "climbId") Integer id,
                                                            @Valid @RequestBody Attempt attempt) {

        // Get the current signed-in user from our database
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Climb updatedClimb = attemptSvc.addAttemptForClimbIdAndUsername(id, currentUser.getUsername(), attempt);

        return new ResponseEntity<>(updatedClimb, HttpStatus.CREATED);

    }

    @PutMapping("/attempts/{attemptId}")
    @Operation(summary = "Update an existing attempt for a user's climb.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The attempt was successfully updated for the user's climb.",
                    content = @Content(schema = @Schema(implementation = Climb.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "BAD_REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    public ResponseEntity<Object> updateAttempt(@PathVariable(name = "climbId") Integer climbId,
                                                @PathVariable(name = "attemptId") Integer attemptId,
                                                @Valid @RequestBody Attempt updatedAttempt) {

        // Get the current signed-in user from our database
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Climb updatedClimb = attemptSvc.updateAttemptByIdForClimbIdAndUsername(attemptId, climbId, currentUser.getUsername(), updatedAttempt);

        return new ResponseEntity<>(updatedClimb, HttpStatus.OK);

    }

    @DeleteMapping("/attempts/{attemptId}")
    @Operation(summary = "Delete an attempt for a user climb.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "The attempt was successfully deleted."
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
    public ResponseEntity deleteAttempt(@PathVariable(name = "climbId") Integer climbId,
                                        @PathVariable(name = "attemptId") Integer attemptId) {

        // Get the current signed-in user from our database
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Get the climbs for this user from our database
        attemptSvc.deleteAttemptByIdForClimbIdAndUsername(climbId, currentUser.getUsername(), attemptId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);

    }

    @PostMapping("/attempts/{attemptId}/attemptImages")
    @Operation(summary = "Add an image to an existing attempt.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "The image was successfully added to the attempt.",
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
    public ResponseEntity<Object> addImageToAttempt(@PathVariable(name = "climbId") Integer climbId,
                                                               @PathVariable(name = "attemptId") Integer attemptId,
                                                               @Valid @RequestBody AttemptImage image) {

        // Get the current signed-in user from our database
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Climb updatedClimb = attemptSvc.addImageToAttempt(climbId, attemptId, currentUser.getUsername(), image);

        return new ResponseEntity<>(updatedClimb, HttpStatus.CREATED);

    }

    @DeleteMapping("/attempts/{attemptId}/attemptImages/{imageId}")
    @Operation(summary = "Delete an attempt image.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "The image was successfully deleted."
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
    public ResponseEntity deleteAttemptImage(@PathVariable(name = "climbId") Integer climbId,
                                        @PathVariable(name = "attemptId") Integer attemptId,
                                        @PathVariable(name = "imageId") Integer imageId) {

        // Get the current signed-in user from our database
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Get the climbs for this user from our database
        attemptSvc.deleteAttemptImage(climbId, attemptId, imageId, currentUser.getUsername());

        return new ResponseEntity(HttpStatus.NO_CONTENT);

    }

}
