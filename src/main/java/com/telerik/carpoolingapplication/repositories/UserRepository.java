package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.models.CreateUserDTO;
import com.telerik.carpoolingapplication.models.User;
import com.telerik.carpoolingapplication.models.UserDTO;

public interface UserRepository {

    void editUser(UserDTO userDTO);

    User getByUsername(String username);

    User getById(int id);

    void createUser(CreateUserDTO userDTO);


}
