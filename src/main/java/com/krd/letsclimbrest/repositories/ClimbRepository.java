package com.krd.letsclimbrest.repositories;

import com.krd.letsclimbrest.entities.Climb;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClimbRepository extends JpaRepository<Climb, Integer> {

    List<Climb> findByUserUsername(String username);

}
