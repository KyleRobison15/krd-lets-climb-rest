package com.krd.letsclimbrest.services;

import com.krd.letsclimbrest.entities.Climb;
import com.krd.letsclimbrest.entities.User;
import com.krd.letsclimbrest.exception.NotFoundException;
import com.krd.letsclimbrest.repositories.ClimbRepository;
import com.krd.letsclimbrest.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    public Climb getClimbByIdAndUsername(Integer id, String username) {

        Climb climb = climbRepo.findClimbByIdAndUserUsername(id, username);

        if(climb == null){
            Map<String, String> exceptionDetails = new HashMap<>();
            exceptionDetails.put("","No climb with id, `" + id + "` found for username, `" + username + "`." );
            throw new NotFoundException("USER_CLIMB_NOT_FOUND", exceptionDetails, "/climbs/" + id);
        }

        return climb;
    }

    @Override
    public void deleteClimbByIdAndUsername(Integer id, String username) {

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

        // Update the creationTs and revisionTs fields
        climb.setCreationTs(LocalDateTime.now(ZoneOffset.UTC));
        climb.setRevisionTs(LocalDateTime.now(ZoneOffset.UTC));

        // Add the climb to the user's list of climbs
        user.addClimb(climb);
        userRepo.saveAndFlush(user);

       return climbRepo.save(climb);

    }

    @Override
    public Climb updateClimbByIdAndUsername(Integer id, String username, Climb updatedClimb) {

        // Check first if the climb exists in the user's list of climbs
        if(!climbRepo.existsByIdAndUserUsername(id, username)){
            Map<String, String> exceptionDetails = new HashMap<>();
            exceptionDetails.put("","No climb with id, `" + id + "` found for username, `" + username + "`." );
            throw new NotFoundException("USER_CLIMB_NOT_FOUND", exceptionDetails, "/climbs/" + id);
        }

        User currentUser = userRepo.findByUsername(username);

        // Get the user's climb that needs to be updated
        Climb existingClimb = climbRepo.findClimbByIdAndUserUsername(id, username);

        existingClimb.setName(updatedClimb.getName());
        existingClimb.setGrade(updatedClimb.getGrade());
        existingClimb.setStyle(updatedClimb.getStyle());
        existingClimb.setPitches(updatedClimb.getPitches());
        existingClimb.setDanger(updatedClimb.getDanger());
        existingClimb.setDescription(updatedClimb.getDescription());
        existingClimb.setStateAbbreviation(updatedClimb.getStateAbbreviation());
        existingClimb.setAreaName(updatedClimb.getAreaName());
        existingClimb.setCragName(updatedClimb.getCragName());
        existingClimb.setCragLongitude(updatedClimb.getCragLongitude());
        existingClimb.setCragLatitude(updatedClimb.getCragLatitude());
        existingClimb.setIsTicked(updatedClimb.getIsTicked());
        existingClimb.setStars(updatedClimb.getStars());
        existingClimb.setFirstSendDate(updatedClimb.getFirstSendDate());
        existingClimb.setImagePath(updatedClimb.getImagePath());
        existingClimb.setRevisionTs(LocalDateTime.now(ZoneOffset.UTC));
        existingClimb.setUser(currentUser);

        // Save the updated climb to the global list of climbs
        return climbRepo.saveAndFlush(existingClimb);
    }

}
