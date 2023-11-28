package com.EmployeeManagement.service;

import com.EmployeeManagement.dto.request.EmployeeRequest;
import com.EmployeeManagement.dto.response.EmployeeResponse;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse saveEmployee(EmployeeRequest request);
    EmployeeResponse getEmployee(int id);
    EmployeeResponse updateEmployee(int id,EmployeeRequest request);
    void deleteEmployee(int id);
    List<EmployeeResponse> getAllEmployees();
}
