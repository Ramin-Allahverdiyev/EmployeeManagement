package com.EmployeeManagement.service;

import com.EmployeeManagement.dto.request.PositionRequest;
import com.EmployeeManagement.dto.response.PositionResponse;
import com.EmployeeManagement.entity.Position;

import java.util.List;
import java.util.Optional;

public interface PositionService {
    Optional<PositionResponse> savePosition(PositionRequest request);
    Optional<PositionResponse> getPosition(int id);
    Position getPositionById(int id);
    Optional<PositionResponse> updatePosition(int id, PositionRequest request);
    void deletePosition(int id);
    List<PositionResponse> getAllPositions();
}
