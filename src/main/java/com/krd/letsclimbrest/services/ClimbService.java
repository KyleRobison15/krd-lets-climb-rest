package com.krd.letsclimbrest.services;

import com.krd.letsclimbrest.entities.Climb;

import java.util.List;

public interface ClimbService {

    Climb createClimb(Climb climb);
    List<Climb> getAllClimbs();

}
