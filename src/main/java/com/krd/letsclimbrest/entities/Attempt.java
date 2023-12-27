package com.krd.letsclimbrest.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @JsonBackReference
    @JsonIgnore
    private Climb climb;

    @OneToMany(mappedBy = "attempt")
    @JsonManagedReference
    private List<AttemptImage> attemptImages;


}
