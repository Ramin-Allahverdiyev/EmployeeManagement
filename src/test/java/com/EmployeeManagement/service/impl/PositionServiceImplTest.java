package com.EmployeeManagement.service.impl;

import com.EmployeeManagement.dto.request.PositionRequest;
import com.EmployeeManagement.entity.Department;
import com.EmployeeManagement.entity.Position;
import com.EmployeeManagement.exception.NotFoundException;
import com.EmployeeManagement.model.ExistStatus;
import com.EmployeeManagement.repository.PositionRepository;
import com.EmployeeManagement.service.DepartmentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@SpringBootTest
class PositionServiceImplTest {
    @Mock
    private PositionRepository positionRepository;
    @Mock
    private DepartmentService departmentService;
    @InjectMocks
    private PositionServiceImpl positionService;

    @DisplayName("According to given id , finding a position")
    @Test
    public void getPositionTest(){
        int id=1;
        var position= Position.builder().id(id).name("Junior").positionStatus(ExistStatus.ACTIVE.getId()).build();

        when(positionRepository.findByIdAndPositionStatus(id, ExistStatus.ACTIVE.getId())).thenReturn(Optional.of(position));

        var positionResponse = positionService.getPosition(id);

        assertNotNull(positionResponse);
        assertEquals(1, positionResponse.get().getId());
        assertEquals("Junior", positionResponse.get().getName());
    }


    @Test
    public void getPositionExceptionTest(){
        int id = 1;
        when(positionRepository.findByIdAndPositionStatus(id, ExistStatus.ACTIVE.getId())).thenReturn(Optional.empty());
        when(positionRepository.findByIdAndPositionStatus(id, ExistStatus.DEACTIVE.getId())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> positionService.getPosition(id));
    }

    @DisplayName("According to given data , save position")
    @Test
    public void savePositionTest(){

        var request= new PositionRequest("Junior",100.0,1);
        var position = Position.builder().name("Junior").build();

        when(positionRepository.save(any(Position.class))).thenReturn(position);

        var positionResponse = positionService.savePosition(request);

        assertNotNull(positionResponse);
    }

    @DisplayName("According to given id , finding all positions")
    @Test
    public void getAllPositionTest(){

        var position1= Position.builder().name("Junior").build();
        var position2= Position.builder().name("Middle").build();

        when(positionRepository.findAllByPositionStatus(ExistStatus.ACTIVE.getId())).thenReturn(Arrays.asList(position1,position2));

        var allPositions = positionService.getAllPositions();

        assertEquals(allPositions.size(),2);

    }

    @DisplayName("According to given id , delete position")
    @Test
    public void deletePositionTest(){
        int id=1;
        var position= Position.builder().id(id).name("Junior").positionStatus(ExistStatus.ACTIVE.getId()).build();
        given(positionRepository.findByIdAndPositionStatus(id,ExistStatus.ACTIVE.getId())).willReturn(Optional.of(position));
        willDoNothing().given(positionRepository).delete(position);

        positionService.deletePosition(id);

        assertThat(position.getPositionStatus()).isEqualTo(ExistStatus.DEACTIVE.getId());
    }

    @Test
    public void deletePositionExceptionTest(){
        int id = 1;
        when(positionRepository.findByIdAndPositionStatus(id, ExistStatus.ACTIVE.getId())).thenReturn(Optional.empty());
        when(positionRepository.findByIdAndPositionStatus(id, ExistStatus.DEACTIVE.getId())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> positionService.getPosition(id));
    }

    @DisplayName("According to given id , update the position")
    @Test
    public void updatePositionTest(){

        int id=1;
        var positionRequest=new PositionRequest("Junior",100.0,1);
        var position = Position.builder().id(id).name("Middle").build();
        var department = Department.builder().id(positionRequest.getDepartmentId()).name("IT").departmentStatus(1).build();
        when(departmentService.getDepartmentById(positionRequest.getDepartmentId())).thenReturn(department);
        given(positionRepository.findByIdAndPositionStatus(id,ExistStatus.ACTIVE.getId())).willReturn(Optional.of(position));
        position.setName(positionRequest.getName());

        when(positionRepository.save(any(Position.class))).thenReturn(position);
        var positionResponse = positionService.updatePosition(id, positionRequest);

        assertNotNull(positionResponse);

    }

    @Test
    public void updatePositionExceptionTest(){
        int id=1;
        var request=new PositionRequest("Junior",100.0,1);
        when(positionRepository.findByIdAndPositionStatus(1,ExistStatus.ACTIVE.getId())).thenReturn(Optional.empty());
        when(positionRepository.findByIdAndPositionStatus(1,ExistStatus.DEACTIVE.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,()-> positionService.updatePosition(id,request));
    }
    @DisplayName("According to given id , finding exception")
    @Test
    public void getPositionByIdTest() {
        int id=1;
        var position= Position.builder().id(id).name("Junior").positionStatus(ExistStatus.ACTIVE.getId()).build();

        when(positionRepository.findById(id)).thenReturn(Optional.of(position));

        var positionById = positionService.getPositionById(id);

        assertNotNull(positionById);
        assertEquals(1, positionById.getId());
        assertEquals("Junior", positionById.getName());
    }

    @Test
    public void getPositionByIdExceptionTest() {
        int id = 1;
        when(positionRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> positionService.getPosition(id));
    }

}