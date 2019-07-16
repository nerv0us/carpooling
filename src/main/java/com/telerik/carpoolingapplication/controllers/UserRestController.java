package com.telerik.carpoolingapplication.controllers;

import com.telerik.carpoolingapplication.models.UserDTO;
import com.telerik.carpoolingapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private UserService service;

    @Autowired
    public UserRestController(UserService service) {
        this.service = service;
    }

    @GetMapping("/{username}")
    public UserDTO getUser(@PathVariable String username) {
        UserDTO user;
        try {
            user = service.getUser(username);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return user;
    }
}
