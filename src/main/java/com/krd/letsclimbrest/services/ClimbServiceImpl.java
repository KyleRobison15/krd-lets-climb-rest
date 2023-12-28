package com.krd.letsclimbrest.services;

import com.krd.letsclimbrest.comparators.GradeComparator;
import com.krd.letsclimbrest.entities.Climb;
import com.krd.letsclimbrest.entities.User;
import com.krd.letsclimbrest.exception.NotFoundException;
import com.krd.letsclimbrest.exception.SortQueryException;
import com.krd.letsclimbrest.repositories.ClimbRepository;
import com.krd.letsclimbrest.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
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

    @Autowired
    AuxillaryService auxSvc;


    @Override
    public List<Climb> getClimbsByUserUsername(String username, String sortBy, String sortOrder) {

        List<Climb> climbs;

        // First check if sortBy query requires sorting by grades
        if (sortBy.equals("grade")) {

            // Sort grades using the custom comparator and sortOrder query param
            try {
                climbs = climbRepo.findByUserUsername(username);
                climbs.sort(new GradeComparator(auxSvc, Sort.Direction.fromString(sortOrder)));
            }
            // Catch the IllegalArgumentException when the sortOrder ENUM is not present in Sort
            // Throw custom sorting exception
            catch (IllegalArgumentException e) {
                Map<String, String> exceptionDetails = new HashMap<>();
                exceptionDetails.put(sortOrder, "sortOrder must be either 'ASC' or 'DESC'.");
                throw new SortQueryException("INVALID_SORT_ORDER", exceptionDetails, "/climbs?sortOrder=");
            }

            return climbs;
        }

        // If sortBy is anything other than "grade", then sort naturally
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
            climbs = climbRepo.findByUserUsername(username, sort);
        }
        // Catch the PropertyReferenceException when the sortBy field is not present in Climb
        // Throw custom sorting exception
        catch (PropertyReferenceException e) {
            Map<String, String> exceptionDetails = new HashMap<>();
            exceptionDetails.put(e.getPropertyName(), "You cannot sort by '" + sortBy + "' because that field does not exist on the Climb entity.");
            throw new SortQueryException("FIELD_NOT_FOUND", exceptionDetails, "/climbs?sortBy=");
        }
        // Catch the IllegalArgumentException when the sortOrder ENUM is not present in Sort
        // Throw custom sorting exception
        catch (IllegalArgumentException e) {
            Map<String, String> exceptionDetails = new HashMap<>();
            exceptionDetails.put(sortOrder, "sortOrder must be either 'ASC' or 'DESC'.");
            throw new SortQueryException("INVALID_SORT_ORDER", exceptionDetails, "/climbs?sortOrder=");
        }

        return climbs;
    }

    @Override
    public Climb getClimbByIdAndUsername(Integer id, String username) {

        Climb climb = climbRepo.findClimbByIdAndUserUsername(id, username);

        if (climb == null) {
            Map<String, String> exceptionDetails = new HashMap<>();
            exceptionDetails.put("", "No climb with id, `" + id + "` found for username, `" + username + "`.");
            throw new NotFoundException("USER_CLIMB_NOT_FOUND", exceptionDetails, "/climbs/" + id);
        }

        return climb;
    }

    @Override
    public void deleteClimbByIdAndUsername(Integer id, String username) {

        Climb climb = climbRepo.findClimbByIdAndUserUsername(id, username);

        if (climb == null) {
            Map<String, String> exceptionDetails = new HashMap<>();
            exceptionDetails.put("", "No climb with id, `" + id + "` found for username, `" + username + "`.");
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
        if (!climbRepo.existsByIdAndUserUsername(id, username)) {
            Map<String, String> exceptionDetails = new HashMap<>();
            exceptionDetails.put("", "No climb with id, `" + id + "` found for username, `" + username + "`.");
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
        existingClimb.setImageFilePath(updatedClimb.getImageFilePath());
        existingClimb.setImageFileName(updatedClimb.getImageFileName());
        existingClimb.setRevisionTs(LocalDateTime.now(ZoneOffset.UTC));
        existingClimb.setUser(currentUser);

        // Save the updated climb to the global list of climbs
        return climbRepo.saveAndFlush(existingClimb);
    }

}
