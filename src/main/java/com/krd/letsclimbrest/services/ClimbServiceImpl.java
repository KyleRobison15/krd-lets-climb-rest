package com.krd.letsclimbrest.services;

import com.krd.letsclimbrest.entities.Climb;
import com.krd.letsclimbrest.entities.User;
import com.krd.letsclimbrest.exception.NotFoundException;
import com.krd.letsclimbrest.repositories.ClimbRepository;
import com.krd.letsclimbrest.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ClimbServiceImpl implements ClimbService {


    @Autowired
    ClimbRepository climbRepo;

    @Autowired
    UserRepository userRepo;

    @Override
    public List<Climb> getClimbsByUserUsername(String username) {
        return climbRepo.findByUserUsername(username);
    }

    @Override
    public Climb getClimbById(Integer id, String username) {

        Climb climb = climbRepo.findClimbByIdAndUserUsername(id, username);

        if(climb == null){
            Map<String, String> exceptionDetails = new HashMap<>();
            exceptionDetails.put("","No climb with id, `" + id + "` found for username, `" + username + "`." );
            throw new NotFoundException("USER_CLIMB_NOT_FOUND", exceptionDetails, "/climbs/" + id);
        }

        return climb;
    }

    @Override
    public void deleteClimbById(Integer id, String username) {

        Climb climb = climbRepo.findClimbByIdAndUserUsername(id, username);

        if(climb == null){
            Map<String, String> exceptionDetails = new HashMap<>();
            exceptionDetails.put("","No climb with id, `" + id + "` found for username, `" + username + "`." );
            throw new NotFoundException("USER_CLIMB_NOT_FOUND", exceptionDetails, "/climbs/" + id);
        }

        climbRepo.deleteById(id);

    }

    @Override
    public Climb createClimbForUser(Climb climb, String username) {

        // Get the current user
        // I am not performing any exception handling here because the user must be authenticated first before they get to this point
        User user = userRepo.findByUsername(username);

        // Add the climb to the user's list of climbs
        user.addClimb(climb);
        userRepo.saveAndFlush(user);

       return climbRepo.save(climb);

    }
}
