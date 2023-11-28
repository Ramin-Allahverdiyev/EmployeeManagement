package com.EmployeeManagement.service;

import com.EmployeeManagement.dto.request.LoginRequest;
import com.EmployeeManagement.dto.request.UserRequest;
import com.EmployeeManagement.dto.response.LoginResponse;
import com.EmployeeManagement.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse saveUser(UserRequest request);
    LoginResponse loginUser(LoginRequest request);
    void deleteUser(int id);
    List<UserResponse> allUsers();
    UserResponse updateUser(int id, UserRequest request);
}
