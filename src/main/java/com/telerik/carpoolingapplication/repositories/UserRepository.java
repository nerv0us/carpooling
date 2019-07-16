package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.models.LoginDTO;
import com.telerik.carpoolingapplication.models.UserDTO;

public interface UserRepository {

    void editUser(UserDTO userDTO);

    UserDTO getUser(String username);

    void authorize(LoginDTO loginDTO);

    void createUser(UserDTO userDTO);

}
