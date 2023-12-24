package com.krd.letsclimbrest.services;

import com.krd.letsclimbrest.entities.Climb;

import java.util.List;

public interface ClimbService {

    Climb createClimbForUser(Climb climb, String username);
    List<Climb> getClimbsByUserUsername(String username);
    Climb getClimbById(Integer id, String username);
    void deleteClimbById(Integer id, String username);

}
