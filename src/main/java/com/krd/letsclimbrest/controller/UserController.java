package com.krd.letsclimbrest.controller;

import com.krd.letsclimbrest.entities.User;
import com.krd.letsclimbrest.repositories.UserRepository;
import com.krd.letsclimbrest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class UserController {

    @Autowired
    UserService userService;

    @DeleteMapping("/admin/users/{id}")
    public void deleteUserById(@PathVariable int id){
        userService.deleteUserById(id);
    }


}
