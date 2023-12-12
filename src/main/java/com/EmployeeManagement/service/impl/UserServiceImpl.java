package com.EmployeeManagement.service.impl;

import com.EmployeeManagement.dto.request.LoginRequest;
import com.EmployeeManagement.dto.request.UserRequest;
import com.EmployeeManagement.dto.response.LoginResponse;
import com.EmployeeManagement.dto.response.UserResponse;
import com.EmployeeManagement.entity.Role;
import com.EmployeeManagement.exception.NotFoundException;
import com.EmployeeManagement.mapper.UserMapper;
import com.EmployeeManagement.model.ExistStatus;
import com.EmployeeManagement.repository.UserRepository;
import com.EmployeeManagement.service.RoleService;
import com.EmployeeManagement.service.UserService;
import com.EmployeeManagement.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RoleService roleService;

    @Override
    public Optional<UserResponse> saveUser(UserRequest request) {
        logger.info("ActionLog.saveUser.start request: {}",request);
        var user = UserMapper.INSTANCE.dtoToEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        var savedUser = userRepository.save(user);
        var userResponse = UserMapper.INSTANCE.entityToDto(savedUser);
        logger.info("ActionLog.saveUser.stop response: {}",userResponse);
        return Optional.of(userResponse);
    }

    @Override
    public Optional<LoginResponse> loginUser(LoginRequest request) {
        logger.info("ActionLog.loginUser.start request: {}",request);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                request.getPassword()));

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException("Username not found!"));
        var loginResponse = LoginResponse
                .builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken(user)).build();
        logger.info("ActionLog.loginUser.stop response: {}",loginResponse);
        return Optional.of(loginResponse);
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
    public Optional<UserResponse> updateUser(int id, UserRequest request) {
        logger.info("ActionLog.updateUser.start id: {}",id);
        List<Role> roles = roleService.getRoles(request.getRoles());
        var user = userRepository.findByIdAndStatus(id, ExistStatus.ACTIVE.isUserStatus())
                .map(u->{
                    u.setName(request.getName());
                    u.setSurname(request.getSurname());
                    u.setUsername(request.getUsername());
                    u.setEmail(request.getEmail());
                    u.setPassword(passwordEncoder.encode(request.getPassword()));
                    u.setRoles(roles);
                    return userRepository.save(u);
                })
                .orElseThrow(() -> new NotFoundException("User is not found for this id!"));
        var response =UserMapper.INSTANCE.entityToDto(user);
        logger.info("ActionLog.updateUser.end id: {}",id);
        return Optional.of(response);
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var user = userRepository.findByUsername(username)
                    .orElseThrow(()->new NotFoundException("User Not Found"));
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateAccessToken(user);
                var loginResponse = LoginResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), loginResponse);
            }
        }
    }
}
