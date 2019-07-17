package com.telerik.carpoolingapplication.controllers;

import com.telerik.carpoolingapplication.models.UserDTO;
import com.telerik.carpoolingapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private UserService service;

    @Autowired
    public UserRestController(UserService service) {
        this.service = service;
    }

    @PutMapping("/update")
    public void editUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            service.editUser(userDTO);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
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

    @PostMapping("/register")
    public UserDTO createUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            service.createUser(userDTO);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
        return userDTO;
    }
}
