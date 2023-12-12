package com.EmployeeManagement.controller;

import com.EmployeeManagement.dto.request.EmployeeRequest;
import com.EmployeeManagement.dto.response.EmployeeResponse;
import com.EmployeeManagement.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("${root.url}/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping("/save")
    public Optional<EmployeeResponse> saveEmployee(@Valid @RequestBody EmployeeRequest request){
        return employeeService.saveEmployee(request);
    }

    @GetMapping("/{id}")
    public Optional<EmployeeResponse> getEmployee(@PathVariable int id){
        return employeeService.getEmployee(id);
    }

    @PutMapping("/{id}")
    public Optional<EmployeeResponse> updateEmployee(@PathVariable int id, @Valid @RequestBody EmployeeRequest request){
        return employeeService.updateEmployee(id,request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable int id){
        employeeService.deleteEmployee(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @GetMapping("/all")
    public List<EmployeeResponse> getAllEmployees(){
        return employeeService.getAllEmployees();
    }
}
