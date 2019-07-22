package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.CreateUserDTO;
import com.telerik.carpoolingapplication.models.User;
import com.telerik.carpoolingapplication.models.UserDTO;
import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String editUser(UserDTO userDTO) {
        User userToEdit = userRepository.getById(userDTO.getId());
        if (userToEdit == null) {
            throw new IllegalArgumentException(String.format(Constants.USER_NOT_FOUND, userDTO.getId()));
        }
        if (!userToEdit.getUsername().equals(userDTO.getUsername())) {
            throw new IllegalArgumentException(Constants.USERNAME_CANNOT_BE_CHANGED_MESSAGE);
        }
        if (userToEdit.getAvatarUri() != null && userDTO.getAvatarUri() != null) {
            userToEdit.setAvatarUri(userDTO.getAvatarUri());
        }
        try {
            userRepository.editUser(userDTO);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(Constants.EMAIL_ALREADY_EXIST, userDTO.getEmail()));
        }
        return Constants.USER_UPDATED;
    }

    @Override
    public User getByUsername(String username) {
        User user = userRepository.getByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException(String.format(Constants.USERNAME_NOT_FOUND, username));
        }
        return user;
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public String createUser(CreateUserDTO userDTO) {
        User user = userRepository.getByUsername(userDTO.getUsername());
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
}
