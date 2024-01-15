package com.krd.letsclimbrest.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String username;

    @NotNull
    @JsonIgnore
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "creation_ts")
    @CreationTimestamp
    private LocalDateTime creationTs;

    @Column(name = "is_active")
    private Boolean isActive;

    private String role;

    @Column(name = "image_file_path")
    private String imageFilePath;

    @Column(name = "image_file_name")
    private String imageFileName;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    @JsonIgnore
    private List<Climb> climbs;

    // Method to add a climb to a user's list of climbs
    public boolean addClimb(Climb climb){
        // Check first if the user's list of climbs is null
        if(climbs == null){
            climbs = new ArrayList<>();
        }

        // If the climb doesn't already exist in their list of climbs, add it to their list
        // Also set the climb's user to this user
        if(!climbs.contains(climb)){
            climbs.add(climb);
            climb.setUser(this);
            // Return true if the climb was added
            return true;
        }

        // If no climb was added, return false
        return false;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(this.role));

        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isActive;
    }
}
