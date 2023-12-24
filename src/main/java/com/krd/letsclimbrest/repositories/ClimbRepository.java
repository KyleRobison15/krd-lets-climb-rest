package com.krd.letsclimbrest.repositories;

import com.krd.letsclimbrest.entities.Climb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClimbRepository extends JpaRepository<Climb, Integer> {

    List<Climb> findByUserUsername(String username);

    Climb findClimbByIdAndUserUsername(Integer id, String username);

}
