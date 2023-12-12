package com.EmployeeManagement.service.impl;

import com.EmployeeManagement.dto.request.EmployeeRequest;
import com.EmployeeManagement.entity.Employee;
import com.EmployeeManagement.entity.Position;
import com.EmployeeManagement.exception.NotFoundException;
import com.EmployeeManagement.model.ExistStatus;
import com.EmployeeManagement.repository.EmployeeRepository;
import com.EmployeeManagement.service.PositionService;
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
import static org.mockito.Mockito.when;

@SpringBootTest
class EmployeeServiceImplTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private PositionService positionService;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @DisplayName("According to given id , finding a employee")
    @Test
    public void getEmployeeTest(){
        int id=1;
        var employee= Employee.builder().id(id).name("Ramin").employeeStatus(ExistStatus.ACTIVE.getId()).build();

        when(employeeRepository.findByIdAndEmployeeStatus(id, ExistStatus.ACTIVE.getId())).thenReturn(Optional.of(employee));

        var employeeResponse = employeeService.getEmployee(id);

        assertNotNull(employeeResponse);
        assertEquals(1, employeeResponse.get().getId());
        assertEquals("Ramin", employeeResponse.get().getName());

    }

    @DisplayName("According to given id , finding exception")
    @Test
    public void getEmployeeExceptionTest(){
        int id=1;
        given(employeeRepository.findByIdAndEmployeeStatus(id,ExistStatus.ACTIVE.getId())).willReturn(Optional.empty());
        assertThrows(NotFoundException.class, ()-> employeeService.getEmployee(id));
    }

    @DisplayName("According to given data , save employee")
    @Test
    public void saveEmployeeTest(){

        var request= new EmployeeRequest("Ramin","Allahverdiyev","ramin@gmail.com",1);
        var employee = Employee.builder().name("Ramin").surname("Allahverdiyev").email("ramin@gmail.com").build();

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        var employeeResponse = employeeService.saveEmployee(request);

        assertNotNull(employeeResponse);
    }

    @DisplayName("Finding all employee")
    @Test
    public void getAllEmployeeTest(){

        var employee1= Employee.builder().name("Ramin").build();
        var employee2= Employee.builder().name("Anar").build();

        when(employeeRepository.findAllByEmployeeStatus(ExistStatus.ACTIVE.getId())).thenReturn(Arrays.asList(employee1,employee2));

        var allEmployees = employeeService.getAllEmployees();

        assertEquals(allEmployees.size(),2);

    }

    @DisplayName("According to given id , delete employee")
    @Test
    public void deleteEmployeeTest(){
        int id=1;
        var employee= Employee.builder().id(id).name("Ramin").build();
        given(employeeRepository.findByIdAndEmployeeStatus(id,ExistStatus.ACTIVE.getId())).willReturn(Optional.of(employee));
        willDoNothing().given(employeeRepository).delete(employee);

        employeeService.deleteEmployee(id);

        assertThat(employee.getEmployeeStatus()).isEqualTo(ExistStatus.DEACTIVE.getId());
    }

    @Test
    public void deleteEmployeeExceptionTest(){
        int id=1;
        when(employeeRepository.findByIdAndEmployeeStatus(id,ExistStatus.ACTIVE.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,()-> employeeService.deleteEmployee(id));
    }

    @DisplayName("According to given id , update the employee")
    @Test
    public void updateEmployeeTest(){

        int id=1;
        var employeeRequest=new EmployeeRequest("Ramin","Allahverdiyev","ramin@gmail.com",1);
        var employee = Employee.builder().id(id).name("Akif").build();
        var position= Position.builder().id(employeeRequest.getPositionId()).name("Junior").salary(1200).build();
        when(positionService.getPositionById(employeeRequest.getPositionId())).thenReturn(position);
        given(employeeRepository.findByIdAndEmployeeStatus(id,ExistStatus.ACTIVE.getId())).willReturn(Optional.of(employee));
        employee.setName(employeeRequest.getName());

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        var employeeResponse = employeeService.updateEmployee(id, employeeRequest);

        assertNotNull(employeeResponse);

    }

    @Test
    public void updateEmployeeExceptionTest(){
        int id=1;
        var employeeRequest=new EmployeeRequest("Ramin","Allahverdiyev","ramin@gmail.com",1);
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,()-> employeeService.updateEmployee(id,employeeRequest));
    }

}