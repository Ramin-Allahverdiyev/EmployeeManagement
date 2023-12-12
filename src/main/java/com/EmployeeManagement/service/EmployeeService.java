package com.EmployeeManagement.service;

import com.EmployeeManagement.dto.request.EmployeeRequest;
import com.EmployeeManagement.dto.response.EmployeeResponse;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Optional<EmployeeResponse> saveEmployee(EmployeeRequest request);
    Optional<EmployeeResponse> getEmployee(int id);
    Optional<EmployeeResponse> updateEmployee(int id, EmployeeRequest request);
    void deleteEmployee(int id);
    List<EmployeeResponse> getAllEmployees();
}
