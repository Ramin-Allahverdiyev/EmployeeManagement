package com.EmployeeManagement.service.impl;

import com.EmployeeManagement.dto.request.LoginRequest;
import com.EmployeeManagement.dto.request.UserRequest;
import com.EmployeeManagement.dto.response.LoginResponse;
import com.EmployeeManagement.dto.response.UserResponse;
import com.EmployeeManagement.exception.NotFoundException;
import com.EmployeeManagement.mapper.UserMapper;
import com.EmployeeManagement.model.ExistStatus;
import com.EmployeeManagement.repository.UserRepository;
import com.EmployeeManagement.service.UserService;
import com.EmployeeManagement.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public UserResponse saveUser(UserRequest request) {
        logger.info("ActionLog.saveUser.start request: {}",request);
        var user = UserMapper.INSTANCE.dtoToEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        var savedUser = userRepository.save(user);
        var userResponse = UserMapper.INSTANCE.entityToDto(savedUser);
        logger.info("ActionLog.saveUser.stop response: {}",userResponse);
        return userResponse;
    }

    @Override
    public LoginResponse loginUser(LoginRequest request) {
        logger.info("ActionLog.loginUser.start request: {}",request);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                request.getPassword()));

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException("Username not found!"));
        var loginResponse = LoginResponse
                .builder()
                .token(jwtService.generateToken(user)).build();
        logger.info("ActionLog.loginUser.stop response: {}",loginResponse);
        return loginResponse;
    }

    @Override
    public void deleteUser(int id) {
        logger.info("ActionLog.deleteUser.start");
        var user = userRepository.findByIdAndStatus(id,ExistStatus.ACTIVE.isUserStatus())
                .orElseThrow(() -> new NotFoundException("User not found!"));
        user.setStatus(ExistStatus.DEACTIVE.isUserStatus());
        userRepository.save(user);
        logger.info("ActionLog.deleteUser.end user deleted");
    }

    @Override
    public List<UserResponse> allUsers() {
        logger.info("ActionLog.allUsers.start");
        var allByStatus = userRepository.findAllByStatus(true);
        var userResponses = UserMapper.INSTANCE.entityListToDtoList(allByStatus);
        logger.info("ActionLog.allUsers.end users count : {}",userResponses.size());
        return userResponses;
    }

    @Override
    public UserResponse updateUser(int id, UserRequest request) {
        logger.info("ActionLog.updateUser.start id: {}",id);
        var user = userRepository.findByIdAndStatus(id, ExistStatus.ACTIVE.isUserStatus())
                .orElseThrow(() -> new NotFoundException("User is not found for this id!"));
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        UserMapper.INSTANCE.dtoToEntity(user,request);
        var updatedUser = userRepository.save(user);
        var response =UserMapper.INSTANCE.entityToDto(updatedUser);
        logger.info("ActionLog.updateUser.end id: {}",id);
        return response;
    }
}
