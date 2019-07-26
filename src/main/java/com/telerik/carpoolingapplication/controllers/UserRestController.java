package com.telerik.carpoolingapplication.controllers;

import com.telerik.carpoolingapplication.exception.CustomException;
import com.telerik.carpoolingapplication.models.*;
import com.telerik.carpoolingapplication.security.JwtTokenProvider;
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
import java.io.IOException;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private UserService userService;
    private FileService fileService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserRestController(UserService service, FileService fileService, JwtTokenProvider jwtTokenProvider) {
        this.userService = service;
        this.fileService = fileService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/{username}")
    public UserDTO getUser(@PathVariable String username) {
        try {
            return userService.getByUsername(username);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/update")
    public void editUser(@Valid @RequestBody UserDTO userDTO, HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        UserDTO user = userService.getByUsername(jwtTokenProvider.getUsername(token));
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (!user.getUsername().equals(jwtTokenProvider.getUsername(token))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        try {
            userService.editUser(userDTO);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("/register")
    public JWTToken createUser(@Valid @RequestBody CreateUserDTO userDTO) {
        try {
            return userService.createUser(userDTO);
        } catch (CustomException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("/authenticate")
    public JWTToken login(@RequestBody LoginDTO user) {
        try {
            return userService.login(user.getUsername(), user.getPassword());
        } catch (CustomException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("/{id}/avatar")
    public void uploadFile(@PathVariable int id, @RequestParam(value = "file") MultipartFile image) {
        try {
            fileService.storeFile(id, image);
        } catch (IllegalArgumentException | IOException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}