package com.EmployeeManagement.repository;

import com.EmployeeManagement.entity.Department;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Integer> {
    List<Department> findAllByDepartmentStatus(int status);
    Optional<Department> findByIdAndDepartmentStatus(int id, int status);
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Position p SET p.positionStatus = 0 WHERE p.department.id = :departmentId")
    void deactivePositionByDepartmentId(@Param("departmentId") Integer departmentId);
}
