package com.krd.letsclimbrest.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.krd.letsclimbrest.exception.ValidFieldValues;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Climb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Name cannot be blank.")
    @Schema(description = "The name of the route.")
    private String name;

    @NotBlank(message = "Grade cannot be blank.")
    @Schema(description = "The route's difficulty rating in Yosemite Decimal System format.")
    private String grade;

    @NotBlank(message = "Style cannot be blank and should be one of the following: 'Trad', 'Sport', 'Boulder', 'Ice', 'Mixed'.")
    @Schema(description = "The style of the route such as 'Trad', 'Sport', 'Boulder', 'Ice', 'Mixed'.")
    @ValidFieldValues(allowedValues = {"Trad", "Sport", "Boulder", "Ice", "Mixed"}, message = "Style must be one of the following case sensitive values: 'Trad', 'Sport', 'Boulder', 'Ice', 'Mixed'.")
    private String style;

    @NotNull
    @Schema(description = "The number of total pitches for the route.")
    private Integer pitches;

    @Schema(description = "The danger level of the route such as 'PG', 'PG-13', 'R', 'X'. Can be left blank if it is the standard level of risk.")
    @ValidFieldValues(allowedValues = {"PG", "PG-13", "R", "X"}, message = "Danger must be blank, null, or one of the following case sensitive values: 'PG', 'PG-13', 'R', 'X'.")
    private String danger;

    @Schema(description = "A brief overview of the route.")
    private String description;

    @Column(name = "state_abbreviation")
    @NotNull
    @Schema(description = "The two character abbreviation of the state the route is in.")
    private String stateAbbreviation;

    @Column(name = "area_name")
    @NotNull
    @Schema(description = "The name of the area where the crag is. For example, Shelf Road is the area where Cactus Cliff crag is.")
    private String areaName;

    @Column(name = "crag_name")
    @NotNull
    @Schema(description = "The name of the crag where the route is. For example, Cactus Cliff is the crag where the route Funkdamental is.")
    private String cragName;

    @Column(name = "crag_longitude")
    @NotNull
    private Double cragLongitude;

    @Column(name = "crag_latitude")
    @NotNull
    private Double cragLatitude;

    @Column(name = "is_ticked")
    @NotNull
    @Schema(description = "True = you have sent the route successfully. False = you have not sent the route successfully.")
    private Boolean isTicked;

    @Min(1)
    @Max(4)
    @Schema(description = "Numeric rating of the quality of the route on a scale of 1 to 4, where 1 is very poor and 4 is a classic.")
    private Integer stars;

    @Column(name = "first_send_date")
    @Schema(description = "The date (yyyy-MM-dd) of the first time you sent the route. Can be left blank if you have not sent it or don't remember when you first sent it.")
    private LocalDate firstSendDate;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "creation_ts")
    @CreationTimestamp
    private LocalDateTime creationTs;

    @Column(name = "revision_ts")
    @CreationTimestamp
    private LocalDateTime revisionTs;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    @JsonIgnore
    private User user;

}
