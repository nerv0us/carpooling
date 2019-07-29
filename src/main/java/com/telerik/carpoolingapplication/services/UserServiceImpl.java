package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.exception.CustomException;
import com.telerik.carpoolingapplication.models.*;
import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.models.enums.Role;
import com.telerik.carpoolingapplication.repositories.UserRepository;
import com.telerik.carpoolingapplication.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider,
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserDTO getByUsername(String username) {
        if (!isUserExist(username)) {
            throw new IllegalArgumentException(String.format(Constants.USERNAME_NOT_FOUND, username));
        }
        User user = userRepository.getByUsername(username);
        return ModelsMapper.getUser(user);
    }

    @Override
    public UserDTO editUser(UserDTO userDTO) {
        if (getById(userDTO.getId()) == null) {
            throw new IllegalArgumentException(String.format(Constants.USER_NOT_FOUND, userDTO.getId()));
        }
        try {
            userRepository.editUser(userDTO);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(Constants.EMAIL_ALREADY_EXIST, userDTO.getEmail()));
        }
        return userDTO;
    }

    @Override
    public JWTToken createUser(CreateUserDTO userDTO) {
        if (isUserExist(userDTO.getUsername())) {
            throw new IllegalArgumentException(String.format(Constants.USERNAME_ALREADY_EXIST, userDTO.getUsername()));
        }

        User user = ModelsMapper.createUser(userDTO);
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ROLE_USER);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        try {
            userRepository.createUser(user);
            return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        } catch (IllegalArgumentException e) {
            throw new CustomException(String.format(Constants.EMAIL_ALREADY_EXIST, userDTO.getEmail()),
                    HttpStatus.CONFLICT);
        }
    }

    @Override
    public JWTToken login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtTokenProvider.createToken(username, userRepository.getByUsername(username).getRoles());
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    private boolean isUserExist(String username) {
        User user = userRepository.getByUsername(username);
        return user != null;
    }
}