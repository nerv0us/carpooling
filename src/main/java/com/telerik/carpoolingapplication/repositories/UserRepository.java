package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.models.CreateUserDTO;
import com.telerik.carpoolingapplication.models.UserDTO;

public interface UserRepository {

    void editUser(UserDTO userDTO);

    UserDTO getUser(String username);

    UserDTO getById(int id);

    void createUser(CreateUserDTO userDTO);

}
