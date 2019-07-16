package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.LoginDTO;
import com.telerik.carpoolingapplication.models.UserDTO;

public interface UserService {

    void editUser(UserDTO userDTO);

    UserDTO getUser(String username);

    void authorize(LoginDTO loginDTO);

    void createUser(UserDTO userDTO);
}
