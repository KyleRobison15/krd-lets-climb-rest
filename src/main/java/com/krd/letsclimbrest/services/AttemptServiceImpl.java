package com.krd.letsclimbrest.services;

import com.krd.letsclimbrest.entities.Attempt;
import com.krd.letsclimbrest.entities.AttemptImage;
import com.krd.letsclimbrest.entities.Climb;
import com.krd.letsclimbrest.exception.NotFoundException;
import com.krd.letsclimbrest.repositories.AttemptImageRepository;
import com.krd.letsclimbrest.repositories.AttemptRepository;
import com.krd.letsclimbrest.repositories.ClimbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@Service
public class AttemptServiceImpl implements AttemptService {

    @Autowired
    ClimbRepository climbRepo;

    @Autowired
    AttemptRepository attemptRepo;

    @Autowired
    AttemptImageRepository attemptImageRepo;

    @Override
    public Climb addAttemptForClimbIdAndUsername(Integer id, String username, Attempt attempt) {

        // Check first if the climb exists in the user's list of climbs
        if (!climbRepo.existsByIdAndUserUsername(id, username)) {
            Map<String, String> exceptionDetails = new HashMap<>();
            exceptionDetails.put("", "No climb with id, `" + id + "` found for username, `" + username + "`.");
            throw new NotFoundException("USER_CLIMB_NOT_FOUND", exceptionDetails, "/climbs/" + id);
        }

        // Get the user's climb
        Climb existingClimb = climbRepo.findClimbByIdAndUserUsername(id, username);

        // Add creation and revision timestamps to the attempt
        attempt.setCreationTs(LocalDateTime.now(ZoneOffset.UTC));
        attempt.setRevisionTs(LocalDateTime.now(ZoneOffset.UTC));

        // Add the attempt to the climb's list of attempts
        existingClimb.addAttempt(attempt);

        // Update the revision timestamp of the climb
        existingClimb.setRevisionTs(LocalDateTime.now(ZoneOffset.UTC));

        // Save the new attempt
        attemptRepo.save(attempt);

        // Save and return the updated climb
        return climbRepo.saveAndFlush(existingClimb);

    }

    @Override
    public Climb updateAttemptByIdForClimbIdAndUsername(Integer attemptId, Integer climbId, String username, Attempt updatedAttempt) {

        // Check first if the climb exists in the user's list of climbs
        if (!climbRepo.existsByIdAndUserUsername(climbId, username)) {
            Map<String, String> exceptionDetails = new HashMap<>();
            exceptionDetails.put("", "No climb with id, `" + climbId + "` found for username, `" + username + "`.");
            throw new NotFoundException("USER_CLIMB_NOT_FOUND", exceptionDetails, "/climbs/" + climbId);
        }

        Climb existingClimb = climbRepo.findClimbByIdAndUserUsername(climbId, username);

        Attempt existingAttempt = attemptRepo.findAttemptByIdAndClimbId(attemptId, climbId);
        existingAttempt.setDate(updatedAttempt.getDate());
        existingAttempt.setDidSend(updatedAttempt.getDidSend());
        existingAttempt.setDescription(updatedAttempt.getDescription());
        existingAttempt.setAttemptImages(updatedAttempt.getAttemptImages());
        existingAttempt.setRevisionTs(LocalDateTime.now(ZoneOffset.UTC));
        existingAttempt.setClimb(existingClimb);

        attemptRepo.saveAndFlush(existingAttempt);

        return climbRepo.saveAndFlush(existingClimb);
    }

    @Override
    public void deleteAttemptByIdForClimbIdAndUsername(Integer climbId, String username, Integer attemptId) {

        Climb climb = climbRepo.findClimbByIdAndUserUsername(climbId, username);
        Attempt attempt = attemptRepo.findAttemptByIdAndClimbId(attemptId, climbId);

        if (climb == null) {
            Map<String, String> exceptionDetails = new HashMap<>();
            exceptionDetails.put("", "No climb with id, `" + climbId + "` found for username, `" + username + "`.");
            throw new NotFoundException("USER_CLIMB_NOT_FOUND", exceptionDetails, "/climbs/" + climbId);
        }

        if (attempt == null) {
            Map<String, String> exceptionDetails = new HashMap<>();
            exceptionDetails.put("", "No attempt with id, `" + attemptId + "` found for climb id, `" + climbId + "`.");
            throw new NotFoundException("ATTEMPT_NOT_FOUND", exceptionDetails, "/climbs/" + climbId + "/attempts/" + attemptId);
        }

        climb.removeAttempt(attempt);
        attemptRepo.deleteById(attemptId);

    }

    @Override
    public Climb addImageToAttempt(Integer climbId, Integer attemptId, String username, AttemptImage image) {

        // Check first if the climb exists in the user's list of climbs
        if (!climbRepo.existsByIdAndUserUsername(climbId, username)) {
            Map<String, String> exceptionDetails = new HashMap<>();
            exceptionDetails.put("", "No climb with id, `" + climbId + "` found for username, `" + username + "`.");
            throw new NotFoundException("USER_CLIMB_NOT_FOUND", exceptionDetails, "/climbs/" + climbId);
        }

        // Check second if the attempt exists for the given climb
        if (!attemptRepo.existsByIdAndClimbId(attemptId, climbId)) {
            Map<String, String> exceptionDetails = new HashMap<>();
            exceptionDetails.put("", "No attempt with id, `" + attemptId + "` found for climb id, `" + climbId + "`.");
            throw new NotFoundException("ATTEMPT_NOT_FOUND", exceptionDetails, "/climbs/" + climbId + "/attempts/" + attemptId);
        }

        // Get the attempt we want to add an image to
        Attempt existingAttempt = attemptRepo.findAttemptByIdAndClimbId(attemptId, climbId);

        // Add the image to the attempt
        existingAttempt.addImage(image);

        // Update revision timestamp for the attempt
        existingAttempt.setRevisionTs(LocalDateTime.now(ZoneOffset.UTC));

        // Save the attempt image
        attemptImageRepo.save(image);

        // Save the updated attempt
        attemptRepo.saveAndFlush(existingAttempt);

        // Return the climb with the added attempt image
        return climbRepo.findClimbByIdAndUserUsername(climbId, username);
    }

    @Override
    public void deleteAttemptImage(Integer climbId, Integer attemptId, Integer imageId, String username) {

        Climb climb = climbRepo.findClimbByIdAndUserUsername(climbId, username);
        Attempt attempt = attemptRepo.findAttemptByIdAndClimbId(attemptId, climbId);
        AttemptImage image = attemptImageRepo.findByIdAndAttemptId(imageId, attemptId);

        if (climb == null) {
            Map<String, String> exceptionDetails = new HashMap<>();
            exceptionDetails.put("", "No climb with id, `" + climbId + "` found for username, `" + username + "`.");
            throw new NotFoundException("USER_CLIMB_NOT_FOUND", exceptionDetails, "/climbs/" + climbId);
        }

        if (attempt == null) {
            Map<String, String> exceptionDetails = new HashMap<>();
            exceptionDetails.put("", "No attempt with id, `" + attemptId + "` found for climb id, `" + climbId + "`.");
            throw new NotFoundException("ATTEMPT_NOT_FOUND", exceptionDetails, "/climbs/" + climbId + "/attempts/" + attemptId);
        }

        if (image == null) {
            Map<String, String> exceptionDetails = new HashMap<>();
            exceptionDetails.put("", "No image with id, `" + imageId + "` found for attempt id, `" + attemptId + "`.");
            throw new NotFoundException("ATTEMPT_IMAGE_NOT_FOUND", exceptionDetails, "/climbs/" + climbId + "/attempts/" + attemptId + "/attemptImages/" + imageId);
        }

        attempt.removeImage(image);
        attemptImageRepo.deleteById(imageId);

    }


}
