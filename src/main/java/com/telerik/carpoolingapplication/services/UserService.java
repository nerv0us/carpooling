package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.CreateUserDTO;
import com.telerik.carpoolingapplication.models.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {

    String editUser(UserDTO userDTO);

    UserDTO getByUsername(String username);

    UserDTO getById(int id);

    String createUser(CreateUserDTO userDTO);

    void storeFile(int userId, MultipartFile file) throws IOException;
}
