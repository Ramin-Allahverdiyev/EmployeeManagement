package com.EmployeeManagement.controller;

import com.EmployeeManagement.dto.request.DepartmentRequest;
import com.EmployeeManagement.dto.response.DepartmentResponse;
import com.EmployeeManagement.service.impl.DepartmentServiceImpl;
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
import java.util.Optional;

import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DepartmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentServiceImpl departmentService;

    private static ObjectMapper objectMapper;
    @BeforeAll
    static void setUp(){
        objectMapper=new ObjectMapper();
    }
    @Test
    void saveDepartmentSuccessTest() throws Exception{
        var request=new DepartmentRequest("IT");
        var response=new DepartmentResponse(7,"IT");
        when(departmentService.saveDepartment(request)).thenReturn(Optional.of(response));

        mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/v1/employee-management/department/save")
                    .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("USER")))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("IT"));

    }

    @Test
    void getAllDepartmentsSuccessTest() throws Exception{
        var response1=new DepartmentResponse(7,"IT");
        var response2=new DepartmentResponse(14,"ACCOUNTING");
        when(departmentService.getAllDepartments()).thenReturn(Arrays.asList(response1,response2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/employee-management/department/all")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("USER"))))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void getDepartmentSuccessTest() throws Exception{
        int id=7;
        var response=new DepartmentResponse(id,"IT");
        when(departmentService.getDepartment(id)).thenReturn(Optional.of(response));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/employee-management/department/{id}",id)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("USER"))))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("IT"));
    }

    @Test
    void updateDepartmentSuccessTest() throws Exception{

        int id=7;
        var request=new DepartmentRequest("IT");
        var response=new DepartmentResponse(7,"Accounting");
        when(departmentService.updateDepartment(id,request)).thenReturn(Optional.of(response));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/employee-management/department/{id}",id)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("Accounting"));
    }

    @Test
    void deleteDepartmentSuccessTest() throws Exception{
        int id=7;
        willDoNothing().given(departmentService).deleteDepartment(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/employee-management/department/{id}",id)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("USER"))))
                .andExpect(status().isNoContent())
                .andDo(print());
    }


}