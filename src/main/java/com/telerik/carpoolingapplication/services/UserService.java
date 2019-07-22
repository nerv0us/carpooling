package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.CreateUserDTO;
import com.telerik.carpoolingapplication.models.User;
import com.telerik.carpoolingapplication.models.UserDTO;

public interface UserService {

    String editUser(UserDTO userDTO);

    User getByUsername(String username);

    User getById(int id);

    String createUser(CreateUserDTO userDTO);

}
