package com.EmployeeManagement.service;

import com.EmployeeManagement.dto.request.RoleRequest;
import com.EmployeeManagement.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse saveRole(RoleRequest request);
    RoleResponse updateRole(int id,RoleRequest request);
    void deleteRole(int id);
    List<RoleResponse> getAllRoles();
}
