package com.krd.letsclimbrest.services;

import com.krd.letsclimbrest.entities.Attempt;
import com.krd.letsclimbrest.entities.AttemptImage;
import com.krd.letsclimbrest.entities.Climb;

public interface AttemptService {

    // Attempts
    Climb addAttemptForClimbIdAndUsername(Integer climbId, String username, Attempt attempt);
    Climb updateAttemptByIdForClimbIdAndUsername(Integer attemptId, Integer climbId, String username, Attempt updatedAttempt);
    void deleteAttemptByIdForClimbIdAndUsername(Integer climbId, String username, Integer attemptId);

    // Attempt Images
    Climb addImageToAttempt(Integer climbId, Integer attemptId, String username, AttemptImage image);
    void deleteAttemptImage(Integer climbId, Integer attemptId, Integer imageId, String username);

}
