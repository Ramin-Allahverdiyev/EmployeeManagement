package com.EmployeeManagement.repository;

import com.EmployeeManagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
    List<Employee> findAllByEmployeeStatus(int status);
    Optional<Employee> findByIdAndEmployeeStatus(int id, int status);
}
