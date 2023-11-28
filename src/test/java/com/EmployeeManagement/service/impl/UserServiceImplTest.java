package com.EmployeeManagement.service.impl;

import com.EmployeeManagement.dto.RoleDto;
import com.EmployeeManagement.dto.request.LoginRequest;
import com.EmployeeManagement.dto.request.UserRequest;
import com.EmployeeManagement.entity.User;
import com.EmployeeManagement.exception.NotFoundException;
import com.EmployeeManagement.model.ExistStatus;
import com.EmployeeManagement.repository.UserRepository;
import com.EmployeeManagement.service.jwt.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserServiceImpl userService;
    @DisplayName("According to given data , save user")
    @Test
    void saveUser() {
        var userRequest = UserRequest.builder().username("test").password("12345").build();
        var user=User.builder().username("test").password("123").build();
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("123");
        when(userRepository.save(any(User.class))).thenReturn(user);

        var userResponse = userService.saveUser(userRequest);

        assertNotNull(userResponse);
        assertEquals("test", userResponse.getUsername());
    }
    @DisplayName("According to given username and password log in!")
    @Test
    void loginUser() {

        var loginRequest = LoginRequest.builder().username("test").password("12345").build();
        var user=User.builder().username("test").password("12345").build();
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("testToken");

        var loginResponse = userService.loginUser(loginRequest);

        assertNotNull(loginResponse);
        assertEquals("testToken", loginResponse.getToken());
    }

    @Test
    void loginUserExceptionTest() {
        var loginRequest = LoginRequest.builder().username("test").password("12345").build();

        when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.loginUser(loginRequest));
    }
    @DisplayName("According to given id , delete user")
    @Test
    void deleteUser() {
        int id = 1;
        var user = User.builder().username("test").status(true).build();

        when(userRepository.findByIdAndStatus(id,ExistStatus.ACTIVE.isUserStatus())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.deleteUser(id);
        assertFalse(user.isStatus());
    }

    @Test
    public void deleteUserExceptionTest(){
        int id=1;

        when(userRepository.findByIdAndStatus(id,ExistStatus.ACTIVE.isUserStatus())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,()-> userService.deleteUser(id));
    }
    @DisplayName("Finding all users")
    @Test
    void allUsers() {
        var user1= User.builder().username("test1").build();
        var user2= User.builder().username("test2").build();

        when(userRepository.findAllByStatus(ExistStatus.ACTIVE.isUserStatus())).thenReturn(Arrays.asList(user1,user2));

        var allUsers = userService.allUsers();

        assertEquals(allUsers.size(),2);
    }

    @DisplayName("According to given id , update the user")
    @Test
    public void updateUserTest(){

        int id=1;
        var userRequest=new UserRequest("Ramin","Rov","ramin@gmail.com","ra1000","12345", Set.of(RoleDto.builder().id(1).build()));
        var user = User.builder().id(id).name("Ramil").build();
        given(userRepository.findByIdAndStatus(id,ExistStatus.ACTIVE.isUserStatus())).willReturn(Optional.of(user));
        user.setName(userRequest.getName());

        when(userRepository.save(any(User.class))).thenReturn(user);
        var movieResponse = userService.updateUser(id, userRequest);

        assertNotNull(movieResponse);

    }

}