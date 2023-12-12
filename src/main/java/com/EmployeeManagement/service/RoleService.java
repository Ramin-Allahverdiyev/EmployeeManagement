package com.EmployeeManagement.service;

import com.EmployeeManagement.dto.RoleDto;
import com.EmployeeManagement.dto.request.RoleRequest;
import com.EmployeeManagement.dto.response.RoleResponse;
import com.EmployeeManagement.entity.Role;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleService {
    Optional<RoleResponse> saveRole(RoleRequest request);
    Optional<RoleResponse> updateRole(int id, RoleRequest request);
    List<Role> getRoles(List<RoleDto> roleDto);
    void deleteRole(int id);
    List<RoleResponse> getAllRoles();
}
