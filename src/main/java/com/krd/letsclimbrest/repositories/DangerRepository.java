package com.krd.letsclimbrest.repositories;

import com.krd.letsclimbrest.entities.Danger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DangerRepository extends JpaRepository<Danger, Integer> {
    List<Danger> findAllByOrderByIdAsc();
}
