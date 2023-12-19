package com.krd.letsclimbrest.controller;

import com.krd.letsclimbrest.entities.Climb;
import com.krd.letsclimbrest.services.ClimbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@CrossOrigin({"*", "http://localhost:8080/*"})
public class ClimbController {

    @Autowired
    private ClimbService climbSvc;

    @PostMapping("/climbs")
    public Climb addClimb(@RequestBody Climb climb){

       return climbSvc.createClimb(climb);

    }

    @GetMapping("/climbs")
    // Get a list of all the climbs in the database, regardless of user
    // NON-AUTHENTICATED
    public List<Climb> getAllClimbs(){
        return climbSvc.getAllClimbs();
    }


}
