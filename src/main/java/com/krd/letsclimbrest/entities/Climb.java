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
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@Schema(description = "Representation of a Climb.")
public class Climb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name cannot be blank.")
    @Schema(description = "The name of the route.", example = "Freerider")
    private String name;

    @Schema(description = "The route's difficulty rating in Yosemite Decimal System format.", example = "5.10c")
    @ValidFieldValues(allowedValues = {"5.4", "5.5", "5.6", "5.7", "5.7+", "5.8-", "5.8", "5.8+", "5.9-", "5.9", "5.9+", "5.10-", "5.10", "5.10+", "5.10a", "5.10b", "5.10c", "5.10d", "5.11-", "5.11", "5.11+", "5.11a", "5.11b", "5.11c", "5.11d", "5.12-", "5.12", "5.12+", "5.12a", "5.12b", "5.12c", "5.12d", "5.13-", "5.13", "5.13+", "5.13a", "5.13b", "5.13c", "5.13d", "5.14-", "5.14", "5.14+", "5.14a", "5.14b", "5.14c", "5.14d", "5.15-", "5.15", "5.15+", "5.15a", "5.15b", "5.15c", "5.15d"},
    message = "Grade must be a valid Yosemite Decimal System climbing grade between 5.4 and 5.15d.")
    private String grade;

    @Column(name = "boulder_grade")
    @Schema(description = "The V-Scale bouldering grade if the route is a boulder problem.", example = "V5")
    @ValidFieldValues(allowedValues = {"V0", "V1", "V2", "V3", "V4", "V5", "V6", "V7", "V8", "V9", "V10", "V11", "V12", "V13", "V14", "V15", "V16", "V17"},
    message = "Boulder Grade must be a valid V-Scale bouldering grade between V0 and V17.")
    private String boulderGrade;

    @NotBlank(message = "Style cannot be blank and should be one of the following: 'Trad', 'Sport', 'Boulder', 'Ice', 'Mixed'.")
    @Schema(description = "The style of the route such as 'Trad', 'Sport', 'Boulder', 'Ice', 'Mixed'.", example = "Trad")
    @ValidFieldValues(allowedValues = {"Trad", "Sport", "Boulder", "Ice", "Mixed"}, message = "Style must be one of the following case sensitive values: 'Trad', 'Sport', 'Boulder', 'Ice', 'Mixed'.")
    private String style;

    @NotNull
    @Schema(description = "The number of total pitches for the route.", example = "5")
    private Integer pitches;

    @Schema(description = "The danger level of the route such as 'PG', 'PG-13', 'R', 'X'. Can be left blank if it is the standard level of risk.", example = "PG-13")
    @ValidFieldValues(allowedValues = {"PG", "PG-13", "R", "X"}, message = "Danger must be blank, null, or one of the following case sensitive values: 'PG', 'PG-13', 'R', 'X'.")
    private String danger;

    @Schema(description = "A brief overview of the route.", example = "This route is one of the 50 classic climbs in North America. It's totally epic!")
    private String description;

    @Column(name = "state_abbreviation")
    @NotNull
    @Schema(description = "The two character abbreviation of the state the route is in.", example = "CO")
    private String stateAbbreviation;

    @Column(name = "area_name")
    @NotNull
    @Schema(description = "The name of the area where the crag is. For example, Shelf Road is the area where Cactus Cliff crag is.", example = "Shelf Road")
    private String areaName;

    @Column(name = "crag_name")
    @NotNull
    @Schema(description = "The name of the crag where the route is. For example, Cactus Cliff is the crag where the route Funkdamental is.", example = "Cactus Cliff")
    private String cragName;

    @Column(name = "crag_longitude")
    @NotNull
    @Schema(description = "The longitudinal coordinate for the crag.", example = "37.73051")
    private Double cragLongitude;

    @Column(name = "crag_latitude")
    @NotNull
    @Schema(description = "The latitudinal coordinate for the crag.", example = "-119.6356")
    private Double cragLatitude;

    @Column(name = "is_ticked")
    @NotNull
    @Schema(description = "True = you have sent the route successfully. False = you have not sent the route successfully.", example = "True")
    private Boolean isTicked;

    @Min(1)
    @Max(4)
    @Schema(description = "Numeric rating of the quality of the route on a scale of 1 to 4, where 1 is very poor and 4 is a classic.", example = "4")
    private Integer stars;

    @Column(name = "first_send_date")
    @Schema(description = "The date (yyyy-MM-dd) of the first time you sent the route. Can be left blank if you have not sent it or don't remember when you first sent it.", example = "2023-11-04")
    private LocalDate firstSendDate;

    @Column(name = "image_file_path")
    @Schema(description = "The file path to the image in the file system.")
    private String imageFilePath;

    @Column(name = "image_file_name")
    @Schema(description = "The name of the image file in the file system.")
    private String imageFileName;

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

    @OneToMany(mappedBy = "climb", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Attempt> attempts;

    // Method to add an attempt to a user's list of attempts for a given climb
    public boolean addAttempt(Attempt attempt){
        // Check first if the user's list of climbs is null
        if(attempts == null){
            attempts = new ArrayList<>();
        }

        // If the attempt doesn't already exist in this climb's list of attempts, add it to the list
        // Also set the attempt's climb to this climb
        if(!attempts.contains(attempt)){
            attempts.add(attempt);
            attempt.setClimb(this);
            // Return true if the attempt was added
            return true;
        }

        // If no attempt was added, return false
        return false;

    }

    // Method to add an attempt to a user's list of attempts for a given climb
    public boolean removeAttempt(Attempt attempt){

        // If the attempt doesn't already exist in this climb's list of attempts, add it to the list
        // Also set the attempt's climb to this climb
        if(attempts != null && attempts.contains(attempt)){
            attempts.remove(attempt);
            // Return true if the attempt was removed
            return true;
        }

        // Return false if no attempt was removed
        return false;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Climb climb = (Climb) o;
        return id != null && Objects.equals(id, climb.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
