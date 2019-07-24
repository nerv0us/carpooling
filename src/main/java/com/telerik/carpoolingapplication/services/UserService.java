package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.JWTToken;
import com.telerik.carpoolingapplication.models.User;
import com.telerik.carpoolingapplication.models.UserDTO;

public interface UserService {

    UserDTO editUser(UserDTO userDTO);

    User getByUsername(String username);

    User getById(int id);

    JWTToken createUser(User user);

    JWTToken login(String username, String password);
}
