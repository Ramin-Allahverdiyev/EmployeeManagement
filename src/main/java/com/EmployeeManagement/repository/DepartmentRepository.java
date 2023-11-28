package com.EmployeeManagement.repository;

import com.EmployeeManagement.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Integer> {
    List<Department> findAllByDepartmentStatus(int status);
    Optional<Department> findByIdAndDepartmentStatus(int id, int status);
}
