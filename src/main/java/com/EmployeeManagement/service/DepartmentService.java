package com.EmployeeManagement.service;

import com.EmployeeManagement.dto.request.DepartmentRequest;
import com.EmployeeManagement.dto.response.DepartmentResponse;
import com.EmployeeManagement.entity.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    Optional<DepartmentResponse> saveDepartment(DepartmentRequest request);
    Optional<DepartmentResponse> getDepartment(int id);
    Department getDepartmentById(int id);
    Optional<DepartmentResponse> updateDepartment(int id, DepartmentRequest request);
    void deleteDepartment(int id);
    List<DepartmentResponse> getAllDepartments();
}
