package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.CreateUserDTO;
import com.telerik.carpoolingapplication.models.JWTToken;
import com.telerik.carpoolingapplication.models.User;
import com.telerik.carpoolingapplication.models.UserDTO;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    UserDTO editUser(UserDTO userDTO, HttpServletRequest request);

    UserDTO getByUsername(String username);

    CreateUserDTO createUser(CreateUserDTO user);

    JWTToken login(String username, String password);

    User getById(int id);
}
