package com.EmployeeManagement.service;

import com.EmployeeManagement.dto.request.PositionRequest;
import com.EmployeeManagement.dto.response.PositionResponse;

import java.util.List;

public interface PositionService {
    PositionResponse savePosition(PositionRequest request);
    PositionResponse getPosition(int id);
    PositionResponse updatePosition(int id,PositionRequest request);
    void deletePosition(int id);
    List<PositionResponse> getAllPositions();
}
