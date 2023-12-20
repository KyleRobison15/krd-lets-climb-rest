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
    public List<Climb> getAllClimbs() {
        return climbRepo.findAll();
    }

    @Override
    public List<Climb> getClimbsByUsername(String username) {
        return climbRepo.findByUserUsername(username);
    }
}
