package com.krd.letsclimbrest.repositories;

import com.krd.letsclimbrest.entities.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttemptRepository extends JpaRepository<Attempt, Integer> {

    Attempt findAttemptByIdAndClimbId(Integer attemptId, Integer climbId);
    boolean existsByIdAndClimbId(Integer attemptId, Integer climbId);

}
