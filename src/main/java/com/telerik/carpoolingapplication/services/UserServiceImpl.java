package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.UserDTO;
import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String editUser(UserDTO userDTO) {
        UserDTO userToEdit = userRepository.getById(userDTO.getId());
        if (userToEdit == null) {
            throw new IllegalArgumentException(String.format(Constants.USER_NOT_FOUND_MESSAGE, userDTO.getId()));
        }
        userRepository.editUser(userDTO);
        return Constants.UPDATED_MESSAGE;
    }

    @Override
    public UserDTO getUser(String username) {
        UserDTO user = userRepository.getUser(username);
        if (user == null) {
            throw new IllegalArgumentException(String.format(Constants.USERNAME_NOT_FOUND_MESSAGE, username));
        }
        return user;
    }

    @Override
    public String createUser(UserDTO userDTO) {
        UserDTO user = userRepository.getUser(userDTO.getUsername());
        if (user != null) {
            throw new IllegalArgumentException(String.format(Constants.USER_ALREADY_EXIST_MESSAGE, userDTO.getUsername()));
        }
        userRepository.createUser(userDTO);
        return Constants.CREATED_MESSAGE;
    }
}
