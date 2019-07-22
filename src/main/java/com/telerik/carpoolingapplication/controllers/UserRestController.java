package com.telerik.carpoolingapplication.controllers;

import com.telerik.carpoolingapplication.models.CreateUserDTO;
import com.telerik.carpoolingapplication.models.User;
import com.telerik.carpoolingapplication.models.UserDTO;
import com.telerik.carpoolingapplication.services.FileService;
import com.telerik.carpoolingapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private UserService userService;
    private FileService fileService;

    @Autowired
    public UserRestController(UserService service, FileService fileService) {
        this.userService = service;
        this.fileService = fileService;
    }

    @PutMapping("/update")
    public void editUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            userService.editUser(userDTO);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.MULTI_STATUS, e.getMessage());
        }
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        User user;
        try {
            user = userService.getByUsername(username);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return user;
    }

    @PostMapping("/register")
    public CreateUserDTO createUser(@Valid @RequestBody CreateUserDTO userDTO) {
        try {
            userService.createUser(userDTO);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
        return userDTO;
    }

    @PostMapping("/{id}/avatar")
    public void uploadFile(@PathVariable int id, @RequestParam("file") MultipartFile file) {
        if (file == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
        }
        try {
            fileService.storeFile(id, file);
        } catch (IllegalArgumentException | IOException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
