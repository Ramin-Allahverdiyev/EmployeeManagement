package com.EmployeeManagement.service.impl;

import com.EmployeeManagement.dto.request.PositionRequest;
import com.EmployeeManagement.dto.response.PositionResponse;
import com.EmployeeManagement.exception.NotFoundException;
import com.EmployeeManagement.mapper.PositionMapper;
import com.EmployeeManagement.model.ExistStatus;
import com.EmployeeManagement.repository.PositionRepository;
import com.EmployeeManagement.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {
    private static final Logger logger= LoggerFactory.getLogger(PositionServiceImpl.class);
    private final PositionRepository positionRepository;

    @Override
    public PositionResponse savePosition(PositionRequest request) {
        logger.info("ActionLog.savePosition.start request: {}",request);
        var position = PositionMapper.INSTANCE.dtoToEntity(request);
        var savedPosition = positionRepository.save(position);
        var positionResponse = PositionMapper.INSTANCE.entityToDto(savedPosition);
        logger.info("ActionLog.savePosition.stop response: {}",positionResponse);
        return positionResponse;
    }

    @Override
    public PositionResponse getPosition(int id) {
        logger.info("ActionLog.getPosition.start id:{}",id);
        var position = positionRepository.findByIdAndPositionStatus(id, ExistStatus.ACTIVE.getId())
                .orElseThrow(() -> new NotFoundException("Position is not found for this id!"));
        var positionResponse = PositionMapper.INSTANCE.entityToDto(position);
        logger.info("ActionLog.getPosition.end id:{}",id);
        return positionResponse;

    }

    @Override
    public PositionResponse updatePosition(int id, PositionRequest request) {
        logger.info("ActionLog.updatePosition.start id: {}",id);
        var position = positionRepository.findByIdAndPositionStatus(id, ExistStatus.ACTIVE.getId())
                .orElseThrow(() -> new NotFoundException("Position is not found for this id!"));
        PositionMapper.INSTANCE.dtoToEntity(position,request);
        var updatedPosition = positionRepository.save(position);
        var response = PositionMapper.INSTANCE.entityToDto(updatedPosition);
        logger.info("ActionLog.updatePosition.end id: {}",id);
        return response;
    }

    @Override
    public void deletePosition(int id) {
        logger.info("ActionLog.deletePosition.start");
        var position = positionRepository.findByIdAndPositionStatus(id, ExistStatus.ACTIVE.getId())
                .orElseThrow(() -> new NotFoundException("Position is not found for this id!"));
        position.setPositionStatus(ExistStatus.DEACTIVE.getId());
        positionRepository.save(position);
        logger.info("ActionLog.deletePosition.end position deleted");
    }

    @Override
    public List<PositionResponse> getAllPositions() {
        logger.info("ActionLog.getAllPositions.start");
        var allPositions = positionRepository.findAllByPositionStatus(ExistStatus.ACTIVE.getId());
        var responses=PositionMapper.INSTANCE.entityListToDtoList(allPositions);
        logger.info("ActionLog.getAllPositions.end positions count : {}",responses.size());
        return responses;
    }
}
