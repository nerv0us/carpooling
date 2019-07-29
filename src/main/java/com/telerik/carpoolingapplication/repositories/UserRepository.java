package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.models.User;
import com.telerik.carpoolingapplication.models.UserDTO;

public interface UserRepository {

    void editUser(UserDTO userDTO);

    User getByUsername(String username);

    User getById(int id);

    void createUser(User user);

    boolean isUsernameExist(String username);

    boolean isEmailExist(String email);
}
