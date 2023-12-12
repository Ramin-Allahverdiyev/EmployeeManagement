package com.EmployeeManagement.service.impl;

import com.EmployeeManagement.dto.RoleDto;
import com.EmployeeManagement.dto.request.LoginRequest;
import com.EmployeeManagement.dto.request.UserRequest;
import com.EmployeeManagement.entity.Role;
import com.EmployeeManagement.entity.User;
import com.EmployeeManagement.exception.NotFoundException;
import com.EmployeeManagement.model.ExistStatus;
import com.EmployeeManagement.repository.UserRepository;
import com.EmployeeManagement.security.JwtService;
import com.EmployeeManagement.service.RoleService;
import com.EmployeeManagement.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleService roleService;
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
        assertEquals("test", userResponse.get().getUsername());
    }
    @DisplayName("According to given username and password log in!")
    @Test
    void loginUser() {

        var loginRequest = LoginRequest.builder().username("test").password("12345").build();
        var user=User.builder().username("test").password("12345").build();
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));
        when(jwtService.generateAccessToken(any(User.class))).thenReturn("testAccessToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("testRefreshToken");

        var loginResponse = userService.loginUser(loginRequest);

        assertNotNull(loginResponse);
        assertEquals("testAccessToken", loginResponse.get().getAccessToken());
        assertEquals("testRefreshToken", loginResponse.get().getRefreshToken());
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
        var userRequest=new UserRequest("Ramin","Rov","ramin@gmail.com","ra1000","12345", List.of(RoleDto.builder().id(1).build()));
        var user = User.builder().id(id).name("Ramil").build();
        var role1=Role.builder().name("ADMIN").id(1).build();
        var role2=Role.builder().name("USER").id(2).build();
        var roles=List.of(role1,role2);
        when(roleService.getRoles(userRequest.getRoles())).thenReturn(roles);
        given(userRepository.findByIdAndStatus(id,ExistStatus.ACTIVE.isUserStatus())).willReturn(Optional.of(user));
        user.setName(userRequest.getName());

        when(userRepository.save(any(User.class))).thenReturn(user);
        var movieResponse = userService.updateUser(id, userRequest);

        assertNotNull(movieResponse);

    }

    @Test
    void refreshTokenTest() throws IOException {
        JwtService jwtService = mock(JwtService.class);
        UserRepository userRepository = mock(UserRepository.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getHeader(AUTHORIZATION)).thenReturn("Bearer testToken");
        when(jwtService.extractUsername("testToken")).thenReturn("testUser");
        User user = new User();
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid("testToken", user)).thenReturn(true);
        when(jwtService.generateAccessToken(user)).thenReturn("newAccessToken");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer.append("newAccessToken"));

        userService.refreshToken(request, response);
        ;
        assertTrue(stringWriter.toString().contains("newAccessToken"));
    }

}