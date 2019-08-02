package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.exceptions.UnauthorizedException;
import com.telerik.carpoolingapplication.exceptions.ValidationException;
import com.telerik.carpoolingapplication.models.dto.CreateUserDTO;
import com.telerik.carpoolingapplication.models.JWTToken;
import com.telerik.carpoolingapplication.models.User;
import com.telerik.carpoolingapplication.models.dto.DriverDTO;
import com.telerik.carpoolingapplication.models.dto.UserDTO;
import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.repositories.UserRepository;
import com.telerik.carpoolingapplication.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           JwtTokenProvider jwtTokenProvider,
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
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
        User user = userRepository.getById(id);
        if (user == null) {
            throw new IllegalArgumentException(String.format(Constants.USER_NOT_FOUND, id));
        }
        return user;
    }

    @Override
    public UserDTO editUser(UserDTO userDTO, HttpServletRequest request) {
        User userToEdit = getById(userDTO.getId());
        if (isNotAuthorized(userDTO.getId(), request)) {
            throw new UnauthorizedException(Constants.UNAUTHORIZED_MESSAGE);
        }
        if (isEmailExist(userDTO.getEmail())
                && !userToEdit.getEmail().equals(userDTO.getEmail())) {
            throw new ValidationException(String.format(Constants.EMAIL_ALREADY_EXIST, userDTO.getEmail()));
        }
        if (userDTO.getAvatarUri() != null) {
            userToEdit.setAvatarUri(userDTO.getAvatarUri());
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
        userRepository.createUser(userDTO);
        return userDTO;

    }

    @Override
    public JWTToken login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            User user = userRepository.getByUsername(username);
            return jwtTokenProvider.createToken(username, user.getRoles());
        } catch (AuthenticationException e) {
            throw new UnauthorizedException(Constants.INVALID_USERNAME_MESSAGE);
        }
    }

    @Override
    public List<DriverDTO> getTopTenDrivers() {
        return userRepository.getTopTenDrivers();
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