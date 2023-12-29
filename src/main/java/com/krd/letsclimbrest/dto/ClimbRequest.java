package com.krd.letsclimbrest.dto;

import com.krd.letsclimbrest.exception.ValidFieldValues;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClimbRequest {

    @NotBlank(message = "Name cannot be blank.")
    @Schema(description = "The name of the route.", example = "Funkdamental")
    private String name;

    @Schema(description = "The route's difficulty rating in Yosemite Decimal System format.", example = "5.11a")
    @ValidFieldValues(allowedValues = {"5.4", "5.5", "5.6", "5.7", "5.7+", "5.8-", "5.8", "5.8+", "5.9-", "5.9", "5.9+", "5.10-", "5.10", "5.10+", "5.10a", "5.10b", "5.10c", "5.10d", "5.11-", "5.11", "5.11+", "5.11a", "5.11b", "5.11c", "5.11d", "5.12-", "5.12", "5.12+", "5.12a", "5.12b", "5.12c", "5.12d", "5.13-", "5.13", "5.13+", "5.13a", "5.13b", "5.13c", "5.13d", "5.14-", "5.14", "5.14+", "5.14a", "5.14b", "5.14c", "5.14d", "5.15-", "5.15", "5.15+", "5.15a", "5.15b", "5.15c", "5.15d"},
            message = "Grade must be a valid Yosemite Decimal System climbing grade between 5.4 and 5.15d.")
    @Nullable
    private String grade;

    @Schema(description = "The V-Scale bouldering grade if the route is a boulder problem. Example: V5", example = "null")
    @ValidFieldValues(allowedValues = {"V0", "V1", "V2", "V3", "V4", "V5", "V6", "V7", "V8", "V9", "V10", "V11", "V12", "V13", "V14", "V15", "V16", "V17", "null"},
            message = "Boulder Grade must be a valid V-Scale bouldering grade between V0 and V17.")
    @Nullable
    private String boulderGrade;

    @NotBlank(message = "Style cannot be blank and should be one of the following: 'Trad', 'Sport', 'Boulder', 'Ice', 'Mixed'.")
    @Schema(description = "The style of the route such as 'Trad', 'Sport', 'Boulder', 'Ice', 'Mixed'.", example = "Sport")
    @ValidFieldValues(allowedValues = {"Trad", "Sport", "Boulder", "Ice", "Mixed"}, message = "Style must be one of the following case sensitive values: 'Trad', 'Sport', 'Boulder', 'Ice', 'Mixed'.")
    private String style;

    @NotNull
    @Schema(description = "The number of total pitches for the route.", example = "1")
    private Integer pitches;

    @Schema(description = "The danger level of the route such as 'PG', 'PG-13', 'R', 'X'. Can be left blank if it is the standard level of risk. Example: PG-13", example = "null")
    @ValidFieldValues(allowedValues = {"PG", "PG-13", "R", "X", "null"}, message = "Danger must be blank, null, or one of the following case sensitive values: 'PG', 'PG-13', 'R', 'X'.")
    private String danger;

    @Schema(description = "A brief overview of the route.", example = "One of the best 5.11s at Shelf Road. This is a must climb for the grade!")
    private String description;

    @NotNull
    @Schema(description = "The two character abbreviation of the state the route is in.", example = "CO")
    private String stateAbbreviation;

    @NotNull
    @Schema(description = "The name of the area where the crag is. For example, Shelf Road is the area where Cactus Cliff crag is.", example = "Shelf Road")
    private String areaName;

    @NotNull
    @Schema(description = "The name of the crag where the route is. For example, Cactus Cliff is the crag where the route Funkdamental is.", example = "Cactus Cliff")
    private String cragName;

    @NotNull
    @Schema(description = "The longitudinal coordinate for the crag.", example = "38.63117")
    private Double cragLongitude;

    @NotNull
    @Schema(description = "The latitudinal coordinate for the crag.", example = "-105.22141")
    private Double cragLatitude;

    @NotNull
    @Schema(description = "True = you have sent the route successfully. False = you have not sent the route successfully.", example = "true")
    private Boolean isTicked;

    @Min(1)
    @Max(4)
    @NotNull
    @Schema(description = "Numeric rating of the quality of the route on a scale of 1 to 4, where 1 is very poor and 4 is a classic.", example = "4")
    private Integer stars;

    @Schema(description = "The date (yyyy-MM-dd) of the first time you sent the route. Can be left blank if you have not sent it or don't remember when you first sent it.", example = "2022-09-20")
    private LocalDate firstSendDate;

    @Schema(description = "The file path to the image in the file system.")
    private String imageFilePath;

    @Schema(description = "The name of the image file in the file system.")
    private String imageFileName;

}
