package com.EmployeeManagement.controller;

import com.EmployeeManagement.dto.request.DepartmentRequest;
import com.EmployeeManagement.dto.response.DepartmentResponse;
import com.EmployeeManagement.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${root.url}/department")
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping("/save")
    public DepartmentResponse saveDepartment(@RequestBody DepartmentRequest request){
        return departmentService.saveDepartment(request);
    }

    @GetMapping("/{id}")
    public DepartmentResponse getDepartment(@PathVariable int id){
        return departmentService.getDepartment(id);
    }

    @PutMapping("/{id}")
    public DepartmentResponse updateDepartment(@PathVariable int id, @RequestBody DepartmentRequest request){
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
