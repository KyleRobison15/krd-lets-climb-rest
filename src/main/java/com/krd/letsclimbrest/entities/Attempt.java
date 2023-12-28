package com.krd.letsclimbrest.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Attempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "The date of the attempt.")
    private LocalDate date;

    @Schema(description = "True or False if the attempt was successful and the user finished the climb.")
    @Column(name = "did_send")
    private Boolean didSend;

    @Schema(description = "An overview of how the attempt went.")
    private String description;

    @Column(name = "creation_ts")
    private LocalDateTime creationTs;

    @Column(name = "revision_ts")
    private LocalDateTime revisionTs;

    @ManyToOne
    @JoinColumn(name = "climb_id")
    @JsonIgnore
    private Climb climb;

    @OneToMany(mappedBy = "attempt", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<AttemptImage> attemptImages;

    // Method to add an image to a user's list of attempt images for a given climb
    public boolean addImage(AttemptImage image){
        // Check first if the user's list of climbs is null
        if(attemptImages == null){
            attemptImages = new ArrayList<>();
        }

        // If the attempt doesn't already exist in this climb's list of attempts, add it to the list
        // Also set the attempt's climb to this climb
        if(!attemptImages.contains(image)){
            attemptImages.add(image);
            image.setAttempt(this);
            // Return true if the attempt was added
            return true;
        }

        // If no attempt was added, return false
        return false;

    }

    // Method to add an attempt to a user's list of attempts for a given climb
    public boolean removeImage(AttemptImage image){

        // If the attempt doesn't already exist in this climb's list of attempts, add it to the list
        // Also set the attempt's climb to this climb
        if(attemptImages != null && attemptImages.contains(image)){
            attemptImages.remove(image);
            // Return true if the attempt was removed
            return true;
        }

        // Return false if no attempt was removed
        return false;

    }


}
