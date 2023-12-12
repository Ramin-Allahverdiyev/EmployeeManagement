package com.EmployeeManagement.controller;

import com.EmployeeManagement.dto.RoleDto;
import com.EmployeeManagement.dto.request.LoginRequest;
import com.EmployeeManagement.dto.request.UserRequest;
import com.EmployeeManagement.dto.response.LoginResponse;
import com.EmployeeManagement.dto.response.UserResponse;
import com.EmployeeManagement.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    private static ObjectMapper objectMapper;
    @BeforeAll
    static void setUp(){
        objectMapper=new ObjectMapper();
    }
    @Test
    void saveUserSuccessTest() throws Exception{
        var request=new UserRequest("Ramin","Rov","ramin@gmail.com","Ra1000","Rr@12345", List.of(RoleDto.builder().id(1).build()));
        var response=new UserResponse(7,"Ramin","Rov","ramin@gmail.com","Ra1000","Rr@12345", List.of(RoleDto.builder().id(1).build()));
        when(userService.saveUser(request)).thenReturn(Optional.of(response));

        mockMvc.perform(post("/api/v1/employee-management/auth/register")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("Ramin"))
                .andExpect(jsonPath("$.username").value("Ra1000"));
    }

    @Test
    void loginUserSuccessTest() throws Exception{
        var request=new LoginRequest("Ramin","12345");
        var response=new LoginResponse("testAccessToken","testRefreshToken");
        when(userService.loginUser(request)).thenReturn(Optional.of(response));

        mockMvc.perform(post("/api/v1/employee-management/auth/login")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.accessToken").value("testAccessToken"))
                .andExpect(jsonPath("$.refreshToken").value("testRefreshToken"));
    }


    @Test
    void getAllPositionsSuccessTest() throws Exception{
        var response1=new UserResponse(7,"Ramin","Rov","ramin@gmail.com","ra1000","12345", List.of(RoleDto.builder().id(1).build()));
        var response2=new UserResponse(7,"Akif","Tov","akif@gmail.com","akif777","12345", List.of(RoleDto.builder().id(2).build()));
        when(userService.allUsers()).thenReturn(Arrays.asList(response1,response2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/employee-management/auth/all")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("USER"))))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void updateUserSuccessTest() throws Exception{

        int id=7;
        var request=new UserRequest("Ramin","Rov","ramin@gmail.com","Ra1000","Rr@12345", List.of(RoleDto.builder().id(1).build()));
        var response=new UserResponse(7,"Ramil","Rov","ramin@gmail.com","Ra1000","Rr@12345", List.of(RoleDto.builder().id(1).build()));
        when(userService.updateUser(id,request)).thenReturn(Optional.of(response));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/employee-management/auth/{id}",id)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("Ramil"))
                .andExpect(jsonPath("$.username").value("Ra1000"));
    }

    @Test
    void deletePositionSuccessTest() throws Exception{
        int id=7;
        willDoNothing().given(userService).deleteUser(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/employee-management/auth/{id}",id)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("USER"))))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void refreshToken() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = new MockHttpServletResponse();

        willDoNothing().given(userService).refreshToken(request, response);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/employee-management/auth/refresh-token")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("USER"))))
                .andExpect(status().isOk())
                .andDo(print());
    }
}