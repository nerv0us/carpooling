package com.telerik.carpoolingapplication.controllers;

import com.telerik.carpoolingapplication.models.CreateUserDTO;
import com.telerik.carpoolingapplication.models.UserDTO;
import com.telerik.carpoolingapplication.services.UserService;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
            throw new ResponseStatusException(HttpStatus.MULTI_STATUS, e.getMessage());
        }
    }

    @GetMapping("/{username}")
    public UserDTO getUser(@PathVariable String username) {
        UserDTO user;
        try {
            user = service.getByUsername(username);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return user;
    }

    @PostMapping("/register")
    public CreateUserDTO createUser(@Valid @RequestBody CreateUserDTO userDTO) {
        try {
            service.createUser(userDTO);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
        return userDTO;
    }

    @PostMapping("/{id}")
    public void uploadFile(@PathVariable int id, @RequestParam("file") MultipartFile file) {
        System.out.println("Uploading...");
        if (file == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Filename is invalid");
        }
        if (isFileValid(file)) {
            throw new InvalidFileNameException(file.getOriginalFilename(), "Invalid file name");
        }
        try {
            service.storeFile(id, file);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    private boolean isFileValid(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        return fileName.contains("..") && fileName.matches("([^\\s]+(\\.(?i)(jpg|jpeg|png|gif))$)");
    }
}
