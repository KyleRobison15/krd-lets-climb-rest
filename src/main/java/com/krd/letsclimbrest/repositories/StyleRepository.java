package com.krd.letsclimbrest.repositories;

import com.krd.letsclimbrest.entities.Style;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StyleRepository extends JpaRepository<Style, Integer> {
    List<Style> findAllByOrderByIdAsc();
}
