package com.telerik.carpoolingapplication.controllers;

import com.telerik.carpoolingapplication.exceptions.UnauthorizedException;
import com.telerik.carpoolingapplication.exceptions.ValidationException;
import com.telerik.carpoolingapplication.models.*;
import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.services.FileService;
import com.telerik.carpoolingapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserRestController(UserService service, FileService fileService, PasswordEncoder passwordEncoder) {
        this.userService = service;
        this.fileService = fileService;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{username}")
    public UserDTO getUser(@PathVariable String username) {
        try {
            User user = userService.getByUsername(username);
            return ModelsMapper.getUser(user);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/update")
    public String editUser(@Valid @RequestBody UserDTO userDTO, HttpServletRequest request) {
        try {
            userService.editUser(userDTO, request);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
        }
        return Constants.USER_UPDATED;
    }

    @PostMapping("/register")
    public String createUser(@Valid @RequestBody CreateUserDTO userDTO) {
        try {
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            userService.createUser(userDTO);
            return Constants.USER_CREATED;
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
        }
    }

    @PostMapping("/authenticate")
    public JWTToken login(@RequestBody LoginDTO user) {
        try {
            return userService.login(user.getUsername(), user.getPassword());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/avatar")
    public void uploadFile(@PathVariable int id, @RequestParam(value = "file") MultipartFile image, HttpServletRequest request) {
        try {
            fileService.storeFile(id, image, request);
        } catch (UnauthorizedException e) {
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