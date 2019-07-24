package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.exception.CustomException;
import com.telerik.carpoolingapplication.models.JWTToken;
import com.telerik.carpoolingapplication.models.User;
import com.telerik.carpoolingapplication.models.UserDTO;
import com.telerik.carpoolingapplication.models.constants.Constants;
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

@Transactional
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO editUser(UserDTO userDTO) {
        User userToEdit = userRepository.getById(userDTO.getId());
        if (userToEdit == null) {
            throw new IllegalArgumentException(String.format(Constants.USER_NOT_FOUND, userDTO.getId()));
        }
        if (!userToEdit.getUsername().equals(userDTO.getUsername())) {
            throw new IllegalArgumentException(Constants.USERNAME_CANNOT_BE_CHANGED_MESSAGE);
        }
        if (userDTO.getAvatarUri() != null) {
            userToEdit.setAvatarUri(userDTO.getAvatarUri());
        }
        try {
            userRepository.editUser(userDTO);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(Constants.EMAIL_ALREADY_EXIST, userDTO.getEmail()));
        }
        return userDTO;
    }

    @Override
    public User getByUsername(String username) {
        User user = userRepository.getByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException(String.format(Constants.USERNAME_NOT_FOUND, username));
        }
        return user;
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public JWTToken createUser(User user) {
        User existingUser = userRepository.getByUsername(user.getUsername());
        if (existingUser != null) {
            throw new IllegalArgumentException(String.format(Constants.USERNAME_ALREADY_EXIST, user.getUsername()));
        }
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setAvatarUri(Constants.DEFAULT_USER_AVATAR_ROUTE);
            userRepository.createUser(user);
            return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(Constants.EMAIL_ALREADY_EXIST, user.getEmail()));
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
}
