package com.EmployeeManagement.service;

import com.EmployeeManagement.dto.request.LoginRequest;
import com.EmployeeManagement.dto.request.UserRequest;
import com.EmployeeManagement.dto.response.LoginResponse;
import com.EmployeeManagement.dto.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserResponse> saveUser(UserRequest request);
    Optional<LoginResponse> loginUser(LoginRequest request);
    void deleteUser(int id);
    List<UserResponse> allUsers();
    Optional<UserResponse> updateUser(int id, UserRequest request);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
