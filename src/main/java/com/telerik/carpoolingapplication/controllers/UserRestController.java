package com.telerik.carpoolingapplication.controllers;

import com.telerik.carpoolingapplication.exception.ForbiddenException;
import com.telerik.carpoolingapplication.exception.ValidationException;
import com.telerik.carpoolingapplication.models.CreateUserDTO;
import com.telerik.carpoolingapplication.models.JWTToken;
import com.telerik.carpoolingapplication.models.LoginDTO;
import com.telerik.carpoolingapplication.models.UserDTO;
import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.services.FileService;
import com.telerik.carpoolingapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.FileNotFoundException;
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

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{username}")
    public UserDTO getUser(@PathVariable String username) {
        try {
            return userService.getByUsername(username);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/update")
    public String editUser(@Valid @RequestBody UserDTO userDTO, HttpServletRequest request) {
        try {
            userService.editUser(userDTO, request);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ForbiddenException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
        }
        return Constants.USER_UPDATED;
    }

    @PostMapping("/register")
    public CreateUserDTO createUser(@Valid @RequestBody CreateUserDTO userDTO) {
        try {
            return userService.createUser(userDTO);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
        }
    }

    @PostMapping("/authenticate")
    public JWTToken login(@RequestBody LoginDTO user) {
        try {
            return userService.login(user.getUsername(), user.getPassword());
        } catch (ForbiddenException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/{id}/avatar")
    public void uploadFile(@PathVariable int id, @RequestParam(value = "file") MultipartFile image, HttpServletRequest request) {
        try {
            fileService.storeFile(id, image, request);
        } catch (ForbiddenException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (FileNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}