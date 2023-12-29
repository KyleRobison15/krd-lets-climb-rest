package com.krd.letsclimbrest.services;

import com.krd.letsclimbrest.comparators.GradeComparator;
import com.krd.letsclimbrest.dto.ClimbRequest;
import com.krd.letsclimbrest.entities.Attempt;
import com.krd.letsclimbrest.entities.Climb;
import com.krd.letsclimbrest.entities.User;
import com.krd.letsclimbrest.exception.DuplicateClimbException;
import com.krd.letsclimbrest.exception.NotFoundException;
import com.krd.letsclimbrest.exception.SortQueryException;
import com.krd.letsclimbrest.repositories.ClimbRepository;
import com.krd.letsclimbrest.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
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
    public List<Climb> getFilteredAndSortedClimbsByUserUsername(String username, String sortBy, String sortOrder, String grade, String boulderGrade,
                                                       String style, String pitchesExpression, String danger, String stateAbbreviation,
                                                       String areaName, String cragName, Boolean isTicked, String starsExpression) {
        // Build up our filter specification
        Specification<Climb> filterSpec = buildFilter(username, grade, boulderGrade, style, pitchesExpression, danger, stateAbbreviation, areaName, cragName, isTicked, starsExpression);

        // First check if sortBy query requires sorting by grades
        if (sortBy.equals("grade")) {
            return sortClimbsByGrade(filterSpec, sortOrder);
        }

        // If sortBy is anything other than "grade", then sort naturally
        return sortClimbs(filterSpec, sortBy, sortOrder);
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
    public Climb createClimbForUser(ClimbRequest climbDto, String username) {

        // Check first if this climb already exists for the user
        if(climbRepo.existsByNameAndCragNameAndUserUsername(climbDto.getName(), climbDto.getCragName(), username)){
            Map<String, String> exceptionDetails = new HashMap<>();
            exceptionDetails.put("name+cragName", "Duplicate climb detected. A climb with name, " + climbDto.getName() + " already exists for crag, " + climbDto.getCragName() + ".");
            throw new DuplicateClimbException("DUPLICATE_CLIMB", exceptionDetails, "/climbs");
        }

        // Build a new climb using the climbDto from the request
        Climb newClimb = Climb.builder()
                .name(climbDto.getName()).grade(climbDto.getGrade()).boulderGrade(climbDto.getBoulderGrade())
                .style(climbDto.getStyle()).pitches(climbDto.getPitches()).danger(climbDto.getDanger())
                .description(climbDto.getDescription()).stateAbbreviation(climbDto.getStateAbbreviation()).areaName(climbDto.getAreaName())
                .cragName(climbDto.getCragName()).cragLongitude(climbDto.getCragLongitude()).cragLatitude(climbDto.getCragLatitude())
                .isTicked(climbDto.getIsTicked()).stars(climbDto.getStars()).firstSendDate(climbDto.getFirstSendDate())
                .imageFilePath(climbDto.getImageFilePath()).imageFileName(climbDto.getImageFileName())
                .build();


        // Get the current user
        // I am not performing any exception handling here because the user must be authenticated first before they get to this point
        User user = userRepo.findByUsername(username);

        // Update the creationTs and revisionTs fields
        newClimb.setCreationTs(LocalDateTime.now(ZoneOffset.UTC));
        newClimb.setRevisionTs(LocalDateTime.now(ZoneOffset.UTC));

        // Set attempts to an empty array list
        if(newClimb.getAttempts() == null){
            List<Attempt> attempts = new ArrayList<>();
            newClimb.setAttempts(attempts);
        }

        // Add the climb to the user's list of climbs
        user.addClimb(newClimb);
        userRepo.saveAndFlush(user);

        return climbRepo.save(newClimb);

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

    private List<Climb> sortClimbsByGrade(Specification<Climb> filterSpec, String sortOrder){

        List<Climb> filteredAndSortedClimbs;
        // Sort grades using the custom comparator and sortOrder query param
        try {
            filteredAndSortedClimbs = climbRepo.findAll(filterSpec);
            filteredAndSortedClimbs.sort(new GradeComparator(auxSvc, Sort.Direction.fromString(sortOrder)));
        }
        // Catch the IllegalArgumentException when the sortOrder ENUM is not present in Sort
        // Throw custom sorting exception
        catch (IllegalArgumentException e) {
            Map<String, String> exceptionDetails = new HashMap<>();
            exceptionDetails.put(sortOrder, "sortOrder must be either 'ASC' or 'DESC'.");
            throw new SortQueryException("INVALID_SORT_ORDER", exceptionDetails, "/climbs?sortOrder=");
        }

        return filteredAndSortedClimbs;
    }

    private List<Climb> sortClimbs(Specification<Climb> filterSpec, String sortBy, String sortOrder){

        List<Climb> sortedClimbs;

        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
            sortedClimbs = climbRepo.findAll(filterSpec, sort);
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

        return sortedClimbs;
    }


    /**
     *
     * This method is used to dynamically build up an SQL query for retrieving climbs.
     * It utilizes the JPA Specifications which allow us to easily create unique SQL query specifications that can be executed using the JpaRepository for a given resource
     *
     * @param username -> Required for retrieving only climbs that belong to the signed in user.
     * @param grade -> Filter by grade (ie. retrieve only climbs for the 5.10a grade)
     * @param style -> Filter by style (ie. retrieve only climbs with a style of "Sport")
     * @param pitchesExpression -> Filter by number of pitches (ie. retrieve only climbs with 3 or more pitches)
     * @param danger -> Filter by danger (ie. retrieve only climbs with a danger of "PG-13")
     * @param stateAbbreviation -> Filter by stateAbbreviation (ie. retrieve only climbs in "CO")
     * @param areaName -> Filter by areaName (ie. retrieve only climbs in the Shelf Road climbing area)
     * @param cragName -> Filter by cragName (ie. retrieve only climbs in the Cactus Cliff crag)
     * @param isTicked -> Filter by isTicked (ie. retrieve only climbs that have been ticked or not ticked)
     * @param starsExpression -> Filter by number of stars (ie. retrieve only climbs with 3 or more stars)
     * @return The final specification for how to filter the list of climbs. Filters are chained together to allow filtering by more than one field at a single time.
     */
    private Specification<Climb> buildFilter(String username, String grade, String boulderGrade, String style, String pitchesExpression,
                                             String danger, String stateAbbreviation, String areaName,
                                             String cragName, Boolean isTicked, String starsExpression){


        Specification<Climb> filterSpec = Specification.where(null);

        if (username != null){
            filterSpec = filterSpec.and(ClimbSpecification.byUserUsername(username));
        }

        if(grade != null){
            filterSpec = filterSpec.and(ClimbSpecification.gradeLike(grade));
        }

        if(boulderGrade != null){
            filterSpec = filterSpec.and(ClimbSpecification.boulderGradeLike(boulderGrade));
        }

        if(style != null){
            filterSpec = filterSpec.and(ClimbSpecification.styleLike(style));
        }

        if(pitchesExpression != null){
            filterSpec = filterSpec.and(ClimbSpecification.pitches(pitchesExpression));
        }

        if(danger != null){
            filterSpec = filterSpec.and(ClimbSpecification.dangerLike(danger));
        }

        if(stateAbbreviation != null){
            filterSpec = filterSpec.and(ClimbSpecification.stateAbbreviationLike(stateAbbreviation));
        }

        if(areaName != null){
            filterSpec = filterSpec.and(ClimbSpecification.areaNameLike(areaName));
        }

        if(cragName != null){
            filterSpec = filterSpec.and(ClimbSpecification.cragNameLike(cragName));
        }

        if(isTicked != null){
            filterSpec = filterSpec.and(ClimbSpecification.tickedIs(isTicked));
        }

        if(starsExpression != null){
            filterSpec = filterSpec.and(ClimbSpecification.starsEqual(starsExpression));
        }

        return filterSpec;

    }

}
