package com.EmployeeManagement.controller;

import com.EmployeeManagement.dto.request.PositionRequest;
import com.EmployeeManagement.dto.response.PositionResponse;
import com.EmployeeManagement.service.impl.PositionServiceImpl;
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
class PositionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PositionServiceImpl positionService;

    private static ObjectMapper objectMapper;
    @BeforeAll
    static void setUp(){
        objectMapper=new ObjectMapper();
    }
    @Test
    void savePositionSuccessTest() throws Exception{
        var request=new PositionRequest("Junior",351.0,1);
        var response=new PositionResponse(7,"Junior",351.0,1);
        when(positionService.savePosition(request)).thenReturn(Optional.of(response));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/employee-management/position/save")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("Junior"))
                .andExpect(jsonPath("$.salary").value(351.0));

    }

    @Test
    void getAllPositionsSuccessTest() throws Exception{
        var response1=new PositionResponse(7,"Junior",100.0,1);
        var response2=new PositionResponse(14,"Middle",200.0,2);
        when(positionService.getAllPositions()).thenReturn(Arrays.asList(response1,response2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/employee-management/position/all")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("USER"))))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void getPositionSuccessTest() throws Exception{
        int id=7;
        var response=new PositionResponse(7,"Middle",200.0,2);
        when(positionService.getPosition(id)).thenReturn(Optional.of(response));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/employee-management/position/{id}",id)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("USER"))))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("Middle"));
    }

    @Test
    void updatePositionSuccessTest() throws Exception{

        int id=7;
        var request=new PositionRequest("Junior",351.0,1);
        var response=new PositionResponse(7,"Middle",351.0,1);
        when(positionService.updatePosition(id,request)).thenReturn(Optional.of(response));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/employee-management/position/{id}",id)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("Middle"))
                .andExpect(jsonPath("$.salary").value(351.0));
    }

    @Test
    void deletePositionSuccessTest() throws Exception{
        int id=7;
        willDoNothing().given(positionService).deletePosition(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/employee-management/position/{id}",id)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("USER"))))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}