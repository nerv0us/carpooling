package com.telerik.carpoolingapplication;

import com.telerik.carpoolingapplication.exceptions.ValidationException;
import com.telerik.carpoolingapplication.models.dto.CreateUserDTO;
import com.telerik.carpoolingapplication.models.User;
import com.telerik.carpoolingapplication.models.dto.UserDTO;
import com.telerik.carpoolingapplication.repositories.UserRepositoryImpl;
import com.telerik.carpoolingapplication.services.UserServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTests {

    @Mock
    UserRepositoryImpl userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    public void getByUsername_Should_ReturnUser_When_Exist() {
        // Arrange
        User user = new User("username", "firstName", "lastName", "user@example.com",
                "08888888", "12345", 0D, 0D, "/images/avatar");
        Mockito.when(userRepository.getByUsername("username")).thenReturn(user);

        //Act
        User result = userService.getByUsername("username");

        //Assert
        Assert.assertEquals(user, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getByUsername_Should_ThrowException_When_User_NotExist() {
        // Arrange

        // Act
        User result = userService.getByUsername("username");

        // Assert
        Mockito.verify(userRepository, Mockito.never()).getByUsername("username");
    }

    @Test
    public void getById_Should_ReturnUser_When_UserExist() {
        // Arrange
        User user = new User("username", "firstName", "lastName", "user@example.com",
                "08888888", "12345", 0D, 0D, "/images/avatar");
        Mockito.when(userRepository.getById(1)).thenReturn(user);

        // Act
        User result = userService.getById(1);

        // Assert
        Assert.assertEquals("username", result.getUsername());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getByIdUser_Should_ThrowException_When_IdNotExists() {
        // Arrange

        // Act
        userService.getById(1);

        // Assert
        Mockito.verify(userRepository, Mockito.never()).getById(1);
    }

    @Test
    public void editUser_Should_CallRepositoryEdit_When_EmailIsUnique() {
        // Arrange
        UserDTO userDTO = new UserDTO(1, "TestUser", "TestUser",
                "username@mail.com", "12345", "08888888", 0D, 0D, "/avatar");
        Mockito.when(userRepository.getById(1)).thenReturn(new User());
//        Mockito.when(userRepository.isEmailExist("username@mail.com")).thenReturn(false);

        HttpServletRequest request = null;

        // Act
        userService.editUser(userDTO, request);

        // Assert
        Mockito.verify(userRepository, Mockito.times(1)).editUser(userDTO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void editUser_Should_ThrowException_When_EmailIsNotUnique() {
        // Arrange
        UserDTO userDTO = new UserDTO(1, "TestUser", "TestUser",
                "username@mail.com", "12345", "08888888", 0D, 0D, "/avatar");
//        Mockito.when(userRepository.isEmailExist("username@mail.com")).thenReturn(true);

        HttpServletRequest request = null;

        // Act
        userService.editUser(userDTO, request);

        // Assert
    }

    @Test
    public void create_Should_CallRepositoryCreate_When_UsernameIsUnique() {
        // Arrange
        CreateUserDTO userDTO = new CreateUserDTO("username", "TestUser", "TestUser",
                "username@mail.com", "12345", "08888888");
        Mockito.when(userRepository.isUsernameExist("username")).thenReturn(false);

        // Act
        userService.createUser(userDTO);

        // Assert
        Mockito.verify(userRepository, Mockito.times(1)).createUser(userDTO);
    }

    @Test(expected = ValidationException.class)
    public void create_Should_ThrowValidationException_When_UsernameIsNotUnique() {
        // Arrange
        CreateUserDTO userDTO = new CreateUserDTO("username", "TestUser", "TestUser",
                "username@mail.com", "12345", "08888888");
        Mockito.when(userRepository.isUsernameExist("username")).thenReturn(true);

        // Act
        userService.createUser(userDTO);

        // Assert
        Mockito.verify(userRepository, Mockito.never()).createUser(userDTO);
    }

    @Test(expected = ValidationException.class)
    public void create_Should_ThrowValidationException_When_EmailIsNotUnique() {
        // Arrange
        CreateUserDTO userDTO = new CreateUserDTO("username", "TestUser", "TestUser",
                "username@mail.com", "12345", "08888888");
        Mockito.when(userRepository.isEmailExist("username@mail.com")).thenReturn(true);

        // Act
        userService.createUser(userDTO);

        // Assert
        Mockito.verify(userRepository, Mockito.never()).createUser(userDTO);
    }
}

