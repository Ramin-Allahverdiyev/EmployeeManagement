package com.EmployeeManagement.controller;

import com.EmployeeManagement.dto.request.RoleRequest;
import com.EmployeeManagement.dto.response.RoleResponse;
import com.EmployeeManagement.service.impl.RoleServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RoleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleServiceImpl roleService;

    private static ObjectMapper objectMapper;
    @BeforeAll
    static void setUp(){
        objectMapper=new ObjectMapper();
    }
    @Test
    void saveRoleSuccessTest() throws Exception{
        var request=new RoleRequest("ADMIN");
        var response=new RoleResponse(7,"ADMIN");
        when(roleService.saveRole(request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/employee-management/role/save")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("ADMIN"));
    }

    @Test
    void getAllRolesSuccessTest() throws Exception{
        var response1=new RoleResponse(7,"ADMIN");
        var response2=new RoleResponse(14,"ADMIN");
        when(roleService.getAllRoles()).thenReturn(Arrays.asList(response1,response2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/employee-management/role/roles")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("USER"))))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void updateRoleSuccessTest() throws Exception{

        int id=7;
        var request=new RoleRequest("ADMIN");
        var response=new RoleResponse(7,"USER");
        when(roleService.updateRole(id,request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/employee-management/role/{id}",id)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("USER"));
    }

    @Test
    void deleteRoleSuccessTest() throws Exception{
        int id=7;
        willDoNothing().given(roleService).deleteRole(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/employee-management/role/{id}",id)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("USER"))))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}