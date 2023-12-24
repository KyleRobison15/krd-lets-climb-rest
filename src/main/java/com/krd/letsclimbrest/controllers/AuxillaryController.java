package com.krd.letsclimbrest.controllers;

import com.krd.letsclimbrest.dto.ErrorResponse;
import com.krd.letsclimbrest.entities.BoulderGrade;
import com.krd.letsclimbrest.entities.Danger;
import com.krd.letsclimbrest.entities.Grade;
import com.krd.letsclimbrest.entities.Style;
import com.krd.letsclimbrest.services.AuxillaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class AuxillaryController {

    @Autowired
    private AuxillaryService auxSvc;

    @GetMapping("/grades")
    @Operation(summary = "Get a list of Yosemite Decimal System climbing grades in order of easiest to hardest.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)))
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
    public ResponseEntity<List<String>> getGrades() {

        // Get the climbs for this user from our database
        List<Grade> grades = auxSvc.getAllGrades();
        List<String> nameArray = grades.stream().map(Grade::getName).toList();

        return ResponseEntity.ok(nameArray);

    }

    @GetMapping("/boulder-grades")
    @Operation(summary = "Get a list of V-Scale bouldering grades in order of easiest to hardest.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)))
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
    public ResponseEntity<List<String>> getBoulderGrades() {

        // Get the climbs for this user from our database
        List<BoulderGrade> boulderGrades = auxSvc.getAllBoulderGrades();
        List<String> nameArray = boulderGrades.stream().map(BoulderGrade::getName).toList();

        return ResponseEntity.ok(nameArray);

    }

    @GetMapping("/styles")
    @Operation(summary = "Get a list of climbing styles.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)))
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
    public ResponseEntity<List<String>> getStyles() {

        // Get the climbs for this user from our database
        List<Style> styles = auxSvc.getAllStyles();
        List<String> nameArray = styles.stream().map(Style::getName).toList();

        return ResponseEntity.ok(nameArray);

    }

    @GetMapping("/dangers")
    @Operation(summary = "Get a list of climbing danger ratings.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)))
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
    public ResponseEntity<List<String>> getDangers() {

        // Get the climbs for this user from our database
        List<Danger> dangers = auxSvc.getAllDangers();
        List<String> nameArray = dangers.stream().map(Danger::getName).toList();

        return ResponseEntity.ok(nameArray);

    }

}
