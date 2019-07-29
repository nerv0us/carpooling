package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.CreateUserDTO;
import com.telerik.carpoolingapplication.models.JWTToken;
import com.telerik.carpoolingapplication.models.UserDTO;

public interface UserService {

    UserDTO editUser(UserDTO userDTO);

    UserDTO getByUsername(String username);

    JWTToken createUser(CreateUserDTO user);

    JWTToken login(String username, String password);
}
