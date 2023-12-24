package com.krd.letsclimbrest.repositories;

import com.krd.letsclimbrest.entities.Climb;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClimbRepository extends JpaRepository<Climb, Integer> {

    List<Climb> findByUserUsername(String username, Sort sort);

    Climb findClimbByIdAndUserUsername(Integer id, String username);

    boolean existsByIdAndUserUsername(Integer id, String username);

}
