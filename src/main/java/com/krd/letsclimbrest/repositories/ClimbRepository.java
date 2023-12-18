package com.krd.letsclimbrest.repositories;

import com.krd.letsclimbrest.entities.Climb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClimbRepository extends JpaRepository<Climb, Integer> {

}
