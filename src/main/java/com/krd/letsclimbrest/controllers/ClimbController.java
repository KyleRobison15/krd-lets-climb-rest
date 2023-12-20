package com.krd.letsclimbrest.controllers;
import com.krd.letsclimbrest.services.ClimbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/climbs")
public class ClimbController {

    @Autowired
    private ClimbService climbSvc;

    @GetMapping
    public ResponseEntity<String> authenticationTest(){
        return  ResponseEntity.ok("Hello from a secured endpoint!");
    }



}
