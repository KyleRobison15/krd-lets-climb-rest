package com.krd.letsclimbrest.repositories;

import com.krd.letsclimbrest.entities.BoulderGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoulderGradeRepository extends JpaRepository<BoulderGrade, Integer> {
    List<BoulderGrade> findAllByOrderByIdAsc();
}
