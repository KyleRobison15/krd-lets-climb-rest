package com.krd.letsclimbrest.repositories;

import com.krd.letsclimbrest.entities.Climb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClimbRepository extends JpaRepository<Climb, Integer>, JpaSpecificationExecutor<Climb> {

    List<Climb> findByUserUsername(String username);

    Climb findClimbByIdAndUserUsername(Integer id, String username);

    boolean existsByIdAndUserUsername(Integer id, String username);

    boolean existsByNameAndCragNameAndUserUsername(String name, String cragName, String username);

}
