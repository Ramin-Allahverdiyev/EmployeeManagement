package com.EmployeeManagement.service;

import com.EmployeeManagement.dto.request.DepartmentRequest;
import com.EmployeeManagement.dto.response.DepartmentResponse;

import java.util.List;

public interface DepartmentService {
    DepartmentResponse saveDepartment(DepartmentRequest request);
    DepartmentResponse getDepartment(int id);
    DepartmentResponse updateDepartment(int id,DepartmentRequest request);
    void deleteDepartment(int id);
    List<DepartmentResponse> getAllDepartments();
}
