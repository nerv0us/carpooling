package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.CreateUserDTO;
import com.telerik.carpoolingapplication.models.UserDTO;
import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private FileService fileService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, FileService fileService) {
        this.userRepository = userRepository;
        this.fileService = fileService;
    }

    @Override
    public String editUser(UserDTO userDTO) {
        UserDTO userToEdit = userRepository.getById(userDTO.getId());
        if (userToEdit == null) {
            throw new IllegalArgumentException(String.format(Constants.USER_NOT_FOUND, userDTO.getId()));
        }
        UserDTO user = userRepository.getByUsername(userDTO.getUsername());
        if (user != null) {
            throw new IllegalArgumentException(String.format(Constants.USERNAME_ALREADY_EXIST, userDTO.getUsername()));
        }
        try {
            userRepository.editUser(userDTO);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(Constants.EMAIL_ALREADY_EXIST, userDTO.getEmail()));
        }
        return Constants.USER_UPDATED;
    }

    @Override
    public UserDTO getByUsername(String username) {
        UserDTO user = userRepository.getByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException(String.format(Constants.USERNAME_NOT_FOUND, username));
        }
        return user;
    }

    @Override
    public UserDTO getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public String createUser(CreateUserDTO userDTO) {
        UserDTO user = userRepository.getByUsername(userDTO.getUsername());
        if (user != null) {
            throw new IllegalArgumentException(String.format(Constants.USERNAME_ALREADY_EXIST, userDTO.getUsername()));
        }
        try {
            userRepository.createUser(userDTO);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(Constants.EMAIL_ALREADY_EXIST, userDTO.getEmail()));
        }
        return Constants.USER_CREATED;
    }

    @Override
    public void storeFile(int userId, MultipartFile file) throws IOException {
        try {
            fileService.storeFile(userId, file);
        } catch (IllegalArgumentException ex) {
            throw new IOException("Failed to store file ", ex);
        }
    }
}
