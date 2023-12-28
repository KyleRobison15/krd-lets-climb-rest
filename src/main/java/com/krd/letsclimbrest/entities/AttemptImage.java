package com.krd.letsclimbrest.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AttemptImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "file_path")
    @Schema(description = "The image file path.")
    @NotBlank
    private String filePath;

    @Column(name = "file_name")
    @Schema(description = "The image file name. Must be valid image file format.")
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_-]+\\.(jpg|jpeg|png|gif|bmp)$")
    private String fileName;

    @ManyToOne
    @JoinColumn(name = "attempt_id")
    @JsonIgnore
    private Attempt attempt;

}
