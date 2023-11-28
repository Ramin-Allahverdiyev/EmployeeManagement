package com.EmployeeManagement.service.impl;

import com.EmployeeManagement.dto.request.RoleRequest;
import com.EmployeeManagement.entity.Role;
import com.EmployeeManagement.exception.NotFoundException;
import com.EmployeeManagement.model.ExistStatus;
import com.EmployeeManagement.repository.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest
class RoleServiceImplTest {
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleServiceImpl roleService;

    @DisplayName("According to given data , save role")
    @Test
    public void saveRoleTest(){

        var request= new RoleRequest("ADMIN");
        var role = Role.builder().name("ADMIN").build();

        when(roleRepository.save(any(Role.class))).thenReturn(role);

        var roleResponse = roleService.saveRole(request);

        assertNotNull(roleResponse);
    }

    @DisplayName("According to given id , finding all roles")
    @Test
    public void getAllRolesTest(){

        var role1= Role.builder().name("ADMIN").build();
        var role2= Role.builder().name("USER").build();

        when(roleRepository.findAllByRoleStatus(ExistStatus.ACTIVE.getId())).thenReturn(Arrays.asList(role1,role2));

        var allRoles = roleService.getAllRoles();

        assertEquals(allRoles.size(),2);
    }

    @DisplayName("According to given id , delete role")
    @Test
    public void deleteRoleTest(){
        int id = 1;
        var role = Role.builder().name("ADMIN").roleStatus(ExistStatus.ACTIVE.getId()).build();

        when(roleRepository.findByIdAndRoleStatus(id, ExistStatus.ACTIVE.getId())).thenReturn(Optional.of(role));
        when(roleRepository.save(role)).thenReturn(role);

        roleService.deleteRole(id);
        assertEquals(0, role.getRoleStatus());
    }

    @Test
    public void deleteRoleExceptionTest(){
        int id=1;
        when(roleRepository.findByIdAndRoleStatus(id,ExistStatus.ACTIVE.getId())).thenReturn(Optional.empty());
        when(roleRepository.findByIdAndRoleStatus(id,ExistStatus.DEACTIVE.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,()-> roleService.deleteRole(id));
    }

    @DisplayName("According to given id , update the role")
    @Test
    public void updateRoleTest(){

        int id=1;
        var roleRequest=new RoleRequest("ADMIN");
        var role = Role.builder().id(id).name("USER").build();
        given(roleRepository.findByIdAndRoleStatus(id,ExistStatus.ACTIVE.getId())).willReturn(Optional.of(role));
        role.setName(roleRequest.getName());

        when(roleRepository.save(any(Role.class))).thenReturn(role);
        var movieResponse = roleService.updateRole(id, roleRequest);

        assertNotNull(movieResponse);

    }

    @Test
    public void updateRoleExceptionTest(){
        int id=1;
        var request=new RoleRequest("ADMIN");
        when(roleRepository.findByIdAndRoleStatus(1,ExistStatus.ACTIVE.getId())).thenReturn(Optional.empty());
        when(roleRepository.findByIdAndRoleStatus(1,ExistStatus.DEACTIVE.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,()-> roleService.updateRole(id,request));
    }

}