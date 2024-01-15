package com.krd.letsclimbrest.controllers;

import com.krd.letsclimbrest.dto.ClimbRequest;
import com.krd.letsclimbrest.dto.ErrorResponse;
import com.krd.letsclimbrest.entities.Climb;
import com.krd.letsclimbrest.exception.InvalidGradeException;
import com.krd.letsclimbrest.services.ClimbService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Validated
@Tag(name = "3. Climbs", description = "REST Endpoints for creating, reading, updating and deleting Climbs for an authenticated user.")
public class ClimbController {

    @Autowired
    private ClimbService climbSvc;

    @GetMapping("/climbs")
    @Operation(summary = "Get a list of climbs for an authenticated user. Optional sorting and filtering query params may be used.")
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
    public ResponseEntity<List<Climb>> getClimbs(@Parameter(name = "sortBy", description = "Optionally sort climbs by the specified field. Default behavior is to sort by when the climb was added. Example: grade")
                                                 @RequestParam(defaultValue = "creationTs", required = false) String sortBy,
                                                 @Parameter(name = "sortOrder", description = "Optionally choose the sort order of ASC or DESC. Default is DESC.")
                                                 @RequestParam(defaultValue = "DESC", required = false) String sortOrder,
                                                 @Parameter(name = "grade", description = "Optionally filter climbs by the specified Yosemite Decimal System grade. Example: 5.10b")
                                                 @RequestParam(required = false) String grade,
                                                 @Parameter(name = "boulderGrade", description = "Optionally filter climbs by the specified V-Scale bouldering grade. Example: V4")
                                                 @RequestParam(required = false) String boulderGrade,
                                                 @Parameter(name = "style", description = "Optionally filter climbs by the specified style. Example: Sport")
                                                 @RequestParam(required = false) String style,
                                                 @Parameter(name = "pitches", description = "Optional expression for filtering by number of pitches. Example: <= 5")
                                                 @RequestParam(required = false) String pitches,
                                                 @Parameter(name = "danger", description = "Optionally filter climbs by the specified danger rating. Example: PG-13")
                                                 @RequestParam(required = false) String danger,
                                                 @Parameter(name = "stateAbbreviation", description = "Optionally filter climbs by the specified state abbreviation Example: CO.")
                                                 @RequestParam(required = false) String stateAbbreviation,
                                                 @Parameter(name = "areaName", description = "Optionally filter climbs by the specified area name. Example: Shelf Road")
                                                 @RequestParam(required = false) String areaName,
                                                 @Parameter(name = "cragName", description = "Optionally filter climbs by the specified crag name. Example: Cactus Cliff")
                                                 @RequestParam(required = false) String cragName,
                                                 @Parameter(name = "isTicked", description = "Optionally filter climbs based on whether they have been ticked. Example: true")
                                                 @RequestParam(required = false) Boolean isTicked,
                                                 @Parameter(name = "stars", description = "Optional expression for filtering by number of stars. Example: >= 3")
                                                 @RequestParam(required = false) String stars) {

        // Get the current signed-in user from our database
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Get the climbs for this user from our database
        List<Climb> userClimbs = climbSvc.getFilteredAndSortedClimbsByUserUsername(currentUser.getUsername(), sortBy, sortOrder,
                grade, boulderGrade, style, pitches, danger, stateAbbreviation,
                areaName, cragName, isTicked, stars);

        return ResponseEntity.ok(userClimbs);
    }

    @GetMapping("/climbs/{climbId}")
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
    public ResponseEntity<Climb> getClimbById(@Valid @NotNull @PathVariable(name = "climbId") Integer id) {

        // Get the current signed-in user from our database
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Get the climbs for this user from our database
        Climb climb = climbSvc.getClimbByIdAndUsername(id, currentUser.getUsername());

        return ResponseEntity.ok(climb);

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
    public ResponseEntity<Object> createClimb(@Valid @RequestBody ClimbRequest climbDto) {

        // Work around to default boulder grade to null for swagger
        if(climbDto.getBoulderGrade().equals("null")){
            climbDto.setBoulderGrade(null);
        }

        // Work around to default danger to null for swagger
        if(climbDto.getDanger().equals("null")){
            climbDto.setDanger(null);
        }

        if(climbDto.getGrade() != null && climbDto.getBoulderGrade() != null){
            Map<String, String> exceptionDetails = new HashMap<>();
            exceptionDetails.put("", "Climbs cannot have BOTH a grade AND boulderGrade. If the climb is a boulder route, only include the boulderGrade and set the grade to null. If the route is not a boulder route, only include the grade and set boulderGrade to null.");
            throw new InvalidGradeException("INVALID_GRADE", exceptionDetails, "/climbs");
        }


        // Get the current signed-in user from our database
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Climb createdClimb = climbSvc.createClimbForUser(climbDto, currentUser.getUsername());

        return new ResponseEntity<>(createdClimb, HttpStatus.CREATED);

    }

    @PutMapping("/climbs/{climbId}")
    @Operation(summary = "Update an existing climb for a user.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The climb was successfully updated.",
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
    public ResponseEntity<Object> updateClimb(@PathVariable(name = "climbId") Integer id, @Valid @RequestBody Climb climb) {

        // Get the current signed-in user from our database
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Climb updatedClimb = climbSvc.updateClimbByIdAndUsername(id, currentUser.getUsername(), climb);

        return new ResponseEntity<>(updatedClimb, HttpStatus.OK);

    }

    @DeleteMapping("/climbs/{climbId}")
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
    public ResponseEntity deleteClimbById(@Valid @NotNull @PathVariable(name = "climbId") Integer id) {

        // Get the current signed-in user from our database
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Get the climbs for this user from our database
        climbSvc.deleteClimbByIdAndUsername(id, currentUser.getUsername());

        return new ResponseEntity(HttpStatus.NO_CONTENT);

    }

}
