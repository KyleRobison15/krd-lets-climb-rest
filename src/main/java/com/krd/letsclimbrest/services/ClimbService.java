package com.krd.letsclimbrest.services;

import com.krd.letsclimbrest.dto.ClimbRequest;
import com.krd.letsclimbrest.entities.Climb;

import java.util.List;

public interface ClimbService {

    Climb createClimbForUser(ClimbRequest climb, String username);

    Climb getClimbByIdAndUsername(Integer id, String username);

    void deleteClimbByIdAndUsername(Integer id, String username);

    Climb updateClimbByIdAndUsername(Integer id, String username, Climb updatedClimb);

    List<Climb> getFilteredAndSortedClimbsByUserUsername(String username, String sortBy, String sortOrder, String grade,
                                                         String boulderGrade, String style, String pitchesExpression, String danger,
                                                         String stateAbbreviation, String areaName, String cragName,
                                                         Boolean isTicked, String starsExpression);

}
