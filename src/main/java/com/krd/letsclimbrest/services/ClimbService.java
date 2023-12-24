package com.krd.letsclimbrest.services;

import com.krd.letsclimbrest.entities.Climb;

import java.util.List;

public interface ClimbService {

    Climb createClimbForUser(Climb climb, String username);
    List<Climb> getClimbsByUserUsername(String username);
    Climb getClimbByIdAndUsername(Integer id, String username);
    void deleteClimbByIdAndUsername(Integer id, String username);
    Climb updateClimbByIdAndUsername(Integer id, String username, Climb updatedClimb);

}
