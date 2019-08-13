package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.models.dto.CreateUserDTO;
import com.telerik.carpoolingapplication.models.User;
import com.telerik.carpoolingapplication.models.dto.DriverDTO;
import com.telerik.carpoolingapplication.models.dto.UserDTO;

import java.util.List;

public interface UserRepository {

    void editUser(UserDTO userDTO);

    User getByUsername(String username);

    User getById(int id);

    void createUser(CreateUserDTO user);

    boolean isUsernameExist(String username);

    boolean isEmailExist(String email);

    List<DriverDTO> getTopTenDrivers();
}
