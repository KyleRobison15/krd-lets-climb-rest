package com.krd.letsclimbrest.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
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

    private String name;

    private String grade;

    private String style;

    private Integer pitches;

    private String description;

    @Column(name = "state_abbreviation")
    private String stateAbbreviation;

    @Column(name = "area_name")
    private String areaName;

    @Column(name = "crag_name")
    private String cragName;

    @Column(name = "crag_longitude")
    private Double cragLongitude;

    @Column(name = "crag_latitude")
    private Double cragLatitude;

    @Column(name = "is_ticked")
    private Boolean isTicked;

    @Column(name = "first_send_date")
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
    private User user;

}
