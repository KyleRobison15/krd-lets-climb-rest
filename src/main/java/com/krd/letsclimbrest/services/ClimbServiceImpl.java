package com.krd.letsclimbrest.services;

import com.krd.letsclimbrest.entities.Climb;
import com.krd.letsclimbrest.repositories.ClimbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClimbServiceImpl implements ClimbService {


    @Autowired
    ClimbRepository climbRepo;

    @Override
    public Climb createClimb(Climb climb) {
        return climbRepo.save(climb);
    }

    @Override
    public List<Climb> getClimbsByUserUsername(String username) {
        return climbRepo.findByUserUsername(username);
    }
}
