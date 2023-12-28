package com.krd.letsclimbrest.repositories;

import com.krd.letsclimbrest.entities.AttemptImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttemptImageRepository extends JpaRepository<AttemptImage, Integer> {

    AttemptImage findByIdAndAttemptId(Integer imageId, Integer attemptId);

}
