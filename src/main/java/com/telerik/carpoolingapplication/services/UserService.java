package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.UserDTO;

public interface UserService {

    String editUser(UserDTO userDTO);

    UserDTO getUser(String username);

    String createUser(UserDTO userDTO);
}
