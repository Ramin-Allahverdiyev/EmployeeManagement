package com.EmployeeManagement.service.impl;
import com.EmployeeManagement.dto.request.DepartmentRequest;
import com.EmployeeManagement.entity.Department;
import com.EmployeeManagement.exception.NotFoundException;
import com.EmployeeManagement.model.ExistStatus;
import com.EmployeeManagement.repository.DepartmentRepository;

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
class DepartmentServiceImplTest {
    @Mock
    private DepartmentRepository departmentRepository;
    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @DisplayName("According to given id , finding a department")
    @Test
    public void getDepartmentTest(){
        int id=1;
        var department= Department.builder().id(id).name("IT").departmentStatus(1).build();

        when(departmentRepository.findByIdAndDepartmentStatus(id,1)).thenReturn(Optional.of(department));

        var departmentResponse = departmentService.getDepartment(id);

        assertNotNull(departmentResponse);
        assertEquals(1, departmentResponse.getId());
        assertEquals("IT", departmentResponse.getName());

    }

    @DisplayName("According to given id , finding exception")
    @Test
    public void getDepartmentExceptionTest(){
        int i= 1;
        when(departmentRepository.findByIdAndDepartmentStatus(i, ExistStatus.ACTIVE.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> departmentService.getDepartment(i));
    }

    @DisplayName("According to given data , save department")
    @Test
    public void saveDepartmentTest(){

        var request= new DepartmentRequest("IT");
        var department = Department.builder().name("Math").build();

        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        var departmentResponse = departmentService.saveDepartment(request);

        assertNotNull(departmentResponse);
    }

    @DisplayName("According to given id , finding all department")
    @Test
    public void getAllDepartmentTest(){

        var department1= Department.builder().name("IT").departmentStatus(1).build();
        var department2= Department.builder().name("Accounting").departmentStatus(1).build();

        when(departmentRepository.findAllByDepartmentStatus(ExistStatus.ACTIVE.getId())).thenReturn(Arrays.asList(department1,department2));

        var allDepartments = departmentService.getAllDepartments();

        assertEquals(allDepartments.size(),2);

    }

    @DisplayName("According to given id , delete department")
    @Test
    public void deleteDepartmentTest(){
        int id=1;
        var department= Department.builder().id(id).name("It").departmentStatus(ExistStatus.ACTIVE.getId()).build();
        given(departmentRepository.findByIdAndDepartmentStatus(id,ExistStatus.ACTIVE.getId())).willReturn(Optional.of(department));
        willDoNothing().given(departmentRepository).delete(department);

        departmentService.deleteDepartment(id);

        assertThat(department.getDepartmentStatus()).isEqualTo(ExistStatus.DEACTIVE.getId());
    }

    @Test
    public void deleteDepartmentExceptionTest(){
        int i= 1;

        when(departmentRepository.findByIdAndDepartmentStatus(i,ExistStatus.ACTIVE.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,()-> departmentService.deleteDepartment(i));
    }

    @DisplayName("According to given id , update the department")
    @Test
    public void updateDepartmentTest(){

        int id=1;
        var departmentRequest=new DepartmentRequest("Accounting");
        var department = Department.builder().id(id).name("It").build();
        given(departmentRepository.findByIdAndDepartmentStatus(id,ExistStatus.ACTIVE.getId())).willReturn(Optional.of(department));
        department.setName(departmentRequest.getName());

        when(departmentRepository.save(any(Department.class))).thenReturn(department);
        var departmentResponse = departmentService.updateDepartment(id, departmentRequest);

        assertNotNull(departmentResponse);

    }

    @Test
    public void updateDepartmentExceptionTest(){
        int i= 1;
        var request=new DepartmentRequest("It");
        when(departmentRepository.findByIdAndDepartmentStatus(i,ExistStatus.ACTIVE.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,()-> departmentService.updateDepartment(i,request));
    }

}