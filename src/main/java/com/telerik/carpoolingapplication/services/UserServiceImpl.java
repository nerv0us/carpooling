package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.LoginDTO;
import com.telerik.carpoolingapplication.models.UserDTO;
import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void editUser(UserDTO userDTO) {

    }

    @Override
    public UserDTO getUser(String username) {
        UserDTO user = userRepository.getUser(username);
        if (user == null) {
            throw new IllegalArgumentException(String.format(Constants.USER_NOT_FOUND_MESSAGE, username));
        }
        return user;
    }

    @Override
    public void authorize(LoginDTO loginDTO) {

    }

    @Override
    public void createUser(UserDTO userDTO) {

    }
}
