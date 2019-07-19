package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.models.CreateUserDTO;
import com.telerik.carpoolingapplication.models.UserDTO;

public interface UserRepository {

    void editUser(UserDTO userDTO);

    UserDTO getByUsername(String username);

    UserDTO getById(int id);

    void createUser(CreateUserDTO userDTO);

    void saveImage(int userId, String imageName);

}
