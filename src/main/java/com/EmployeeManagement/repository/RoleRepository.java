package com.EmployeeManagement.repository;

import com.EmployeeManagement.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findByIdAndRoleStatus(int id, int status);
    List<Role> findAllByRoleStatus(int seatStatus);
}
