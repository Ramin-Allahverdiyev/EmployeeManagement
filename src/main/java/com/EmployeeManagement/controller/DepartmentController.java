package com.EmployeeManagement.controller;

import com.EmployeeManagement.dto.request.DepartmentRequest;
import com.EmployeeManagement.dto.response.DepartmentResponse;
import com.EmployeeManagement.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("${root.url}/department")
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping("/save")
    public Optional<DepartmentResponse> saveDepartment(@Valid @RequestBody DepartmentRequest request){
        return departmentService.saveDepartment(request);
    }

    @GetMapping("/{id}")
    public Optional<DepartmentResponse> getDepartment(@PathVariable int id){
        return departmentService.getDepartment(id);
    }

    @PutMapping("/{id}")
    public Optional<DepartmentResponse> updateDepartment(@PathVariable int id, @Valid @RequestBody DepartmentRequest request){
        return departmentService.updateDepartment(id,request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable int id){
        departmentService.deleteDepartment(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @GetMapping("/all")
    public List<DepartmentResponse> getAllDepartments(){
        return departmentService.getAllDepartments();
    }

}
