package com.EmployeeManagement.controller;

import com.EmployeeManagement.dto.request.LoginRequest;
import com.EmployeeManagement.dto.request.UserRequest;
import com.EmployeeManagement.dto.response.LoginResponse;
import com.EmployeeManagement.dto.response.UserResponse;
import com.EmployeeManagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${root.url}/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public UserResponse saveUser(@RequestBody UserRequest request){
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
    public LoginResponse loginUser(@RequestBody LoginRequest request){
        return userService.loginUser(request);
    }

    @PutMapping("/{id}")
    public UserResponse loginUser(@PathVariable int id,@RequestBody UserRequest request){
        return userService.updateUser(id,request);
    }

}
