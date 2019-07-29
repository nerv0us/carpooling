package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.exception.UnauthorizedException;
import com.telerik.carpoolingapplication.exception.ValidationException;
import com.telerik.carpoolingapplication.models.*;
import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.models.enums.Role;
import com.telerik.carpoolingapplication.repositories.UserRepository;
import com.telerik.carpoolingapplication.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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
        if (!isUsernameExist(username)) {
            throw new IllegalArgumentException(String.format(Constants.USERNAME_NOT_FOUND, username));
        }
        User user = userRepository.getByUsername(username);
        return ModelsMapper.getUser(user);
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public UserDTO editUser(UserDTO userDTO, HttpServletRequest request) {
        if (getById(userDTO.getId()) == null) {
            throw new IllegalArgumentException(String.format(Constants.USER_NOT_FOUND, userDTO.getId()));
        }
        if (isNotAuthorized(userDTO.getId(), request)) {
            throw new UnauthorizedException(Constants.UNAUTHORIZED_MESSAGE);
        }
        if (isEmailExist(userDTO.getEmail())) {
            throw new ValidationException(String.format(Constants.EMAIL_ALREADY_EXIST, userDTO.getEmail()));
        }
        userRepository.editUser(userDTO);
        return userDTO;
    }

    @Override
    public CreateUserDTO createUser(CreateUserDTO userDTO) {
        if (isUsernameExist(userDTO.getUsername())) {
            throw new ValidationException(String.format(Constants.USERNAME_ALREADY_EXIST, userDTO.getUsername()));
        }
        if (isEmailExist(userDTO.getEmail())) {
            throw new ValidationException(String.format(Constants.EMAIL_ALREADY_EXIST, userDTO.getEmail()));
        }

        User user = ModelsMapper.createUser(userDTO);
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ROLE_USER);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        userRepository.createUser(user);
        return userDTO;

    }

    @Override
    public JWTToken login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtTokenProvider.createToken(username, userRepository.getByUsername(username).getRoles());
        } catch (AuthenticationException e) {
            throw new UnauthorizedException(Constants.INVALID_USERNAME_MESSAGE);
        }
    }

    private boolean isNotAuthorized(int id, HttpServletRequest request) {
        User user = getById(id);
        String token = jwtTokenProvider.resolveToken(request);
        return !user.getUsername().equals(jwtTokenProvider.getUsername(token));
    }

    private boolean isUsernameExist(String username) {
        return userRepository.isUsernameExist(username);
    }

    private boolean isEmailExist(String email) {
        return userRepository.isEmailExist(email);
    }
}