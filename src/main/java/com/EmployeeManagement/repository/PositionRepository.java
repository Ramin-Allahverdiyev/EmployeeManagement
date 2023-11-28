package com.EmployeeManagement.repository;

import com.EmployeeManagement.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position,Integer> {
    List<Position> findAllByPositionStatus(int status);
    Optional<Position> findByIdAndPositionStatus(int id, int status);
}
