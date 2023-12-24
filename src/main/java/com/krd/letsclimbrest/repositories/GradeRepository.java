package com.krd.letsclimbrest.repositories;

import com.krd.letsclimbrest.entities.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Integer> {
    List<Grade> findAllByOrderByIdAsc();
}
