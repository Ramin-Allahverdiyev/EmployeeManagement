package com.EmployeeManagement.controller;

import com.EmployeeManagement.dto.request.EmployeeRequest;
import com.EmployeeManagement.dto.response.EmployeeResponse;
import com.EmployeeManagement.service.impl.EmployeeServiceImpl;
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
class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeServiceImpl employeeService;

    private static ObjectMapper objectMapper;
    @BeforeAll
    static void setUp(){
        objectMapper=new ObjectMapper();
    }
    @Test
    void saveEmployeeSuccessTest() throws Exception{
        var request=new EmployeeRequest("Ramin","Rov","ramin@gmail.com",1);
        var response=new EmployeeResponse(7,"Ramin","Rov","ramin@gmail.com",1);
        when(employeeService.saveEmployee(request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/employee-management/employee/save")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("Ramin"))
                .andExpect(jsonPath("$.surname").value("Rov"));

    }

    @Test
    void getAllEmployeesSuccessTest() throws Exception{
        var response1=new EmployeeResponse(7,"Ramin","Rov","ramin@gmail.com",1);
        var response2=new EmployeeResponse(14,"Akif","Quliyev","akif@gmail.com",2);
        when(employeeService.getAllEmployees()).thenReturn(Arrays.asList(response1,response2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/employee-management/employee/all")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("USER"))))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void getEmployeeSuccessTest() throws Exception{
        int id=7;
        var response=new EmployeeResponse(7,"Ramin","Rov","ramin@gmail.com",1);
        when(employeeService.getEmployee(id)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/employee-management/employee/{id}",id)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("USER"))))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("Ramin"));
    }

    @Test
    void updateEmployeeSuccessTest() throws Exception{

        int id=7;
        var request=new EmployeeRequest("Ramin","Rov","ramin@gmail.com",1);
        var response=new EmployeeResponse(7,"Ramil","Mov","ramin@gmail.com",1);
        when(employeeService.updateEmployee(id,request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/employee-management/employee/{id}",id)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("Ramil"))
                .andExpect(jsonPath("$.surname").value("Mov"));
    }

    @Test
    void deleteEmployeeSuccessTest() throws Exception{
        int id=7;
        willDoNothing().given(employeeService).deleteEmployee(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/employee-management/employee/{id}",id)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("USER"))))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}