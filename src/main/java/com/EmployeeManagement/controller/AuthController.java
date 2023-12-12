package com.EmployeeManagement.controller;

import com.EmployeeManagement.dto.request.LoginRequest;
import com.EmployeeManagement.dto.request.UserRequest;
import com.EmployeeManagement.dto.response.LoginResponse;
import com.EmployeeManagement.dto.response.UserResponse;
import com.EmployeeManagement.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("${root.url}/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public Optional<UserResponse> saveUser(@RequestBody @Valid UserRequest request){
        return userService.saveUser(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id){
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/all")
    public List<UserResponse> allUser(){
        return userService.allUsers();
    }

    @PostMapping("/login")
    public Optional<LoginResponse> loginUser(@RequestBody LoginRequest request){
        return userService.loginUser(request);
    }

    @PutMapping("/{id}")
    public Optional<UserResponse> updateUser(@PathVariable int id,@Valid @RequestBody UserRequest request){
        return userService.updateUser(id,request);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userService.refreshToken(request,response);
    }

}
