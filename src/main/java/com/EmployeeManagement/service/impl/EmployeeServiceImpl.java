package com.EmployeeManagement.service.impl;

import com.EmployeeManagement.dto.request.EmployeeRequest;
import com.EmployeeManagement.dto.response.EmployeeResponse;
import com.EmployeeManagement.exception.NotFoundException;
import com.EmployeeManagement.mapper.EmployeeMapper;
import com.EmployeeManagement.model.ExistStatus;
import com.EmployeeManagement.repository.EmployeeRepository;
import com.EmployeeManagement.service.EmployeeService;
import com.EmployeeManagement.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger logger= LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private final EmployeeRepository employeeRepository;
    private final PositionService positionService;

    @Override
    public Optional<EmployeeResponse> saveEmployee(EmployeeRequest request) {
        logger.info("ActionLog.saveEmployee.start request: {}",request);
        var employee = EmployeeMapper.INSTANCE.dtoToEntity(request);
        var savedEmployee = employeeRepository.save(employee);
        var response= EmployeeMapper.INSTANCE.entityToDto(savedEmployee);
        logger.info("ActionLog.saveEmployee.stop response: {}",response);
        return Optional.of(response);

    }

    @Override
    public Optional<EmployeeResponse> getEmployee(int id) {
        logger.info("ActionLog.getEmployee.start id:{}",id);
        var employee = employeeRepository.findByIdAndEmployeeStatus(id, ExistStatus.ACTIVE.getId())
                .orElseThrow(() -> new NotFoundException("Employee is not found for this id!"));
        var response= EmployeeMapper.INSTANCE.entityToDto(employee);
        logger.info("ActionLog.getEmployee.end id:{}",id);
        return Optional.of(response);
    }

    @Override
    public Optional<EmployeeResponse> updateEmployee(int id, EmployeeRequest request) {
        logger.info("ActionLog.updateEmployee.start id: {}",id);
        var positionById = positionService.getPositionById(request.getPositionId());
        var employee = employeeRepository.findByIdAndEmployeeStatus(id, ExistStatus.ACTIVE.getId())
                .map(e->{
                    e.setName(request.getName());
                    e.setSurname(request.getSurname());
                    e.setEmail(request.getEmail());
                    e.setPosition(positionById);
                    return employeeRepository.save(e);
                })
                .orElseThrow(() -> new NotFoundException("Employee is not found for this id!"));
        var response =EmployeeMapper.INSTANCE.entityToDto(employee);
        logger.info("ActionLog.updateEmployee.end id: {}",id);
        return Optional.of(response);
    }

    @Override
    public void deleteEmployee(int id) {
        logger.info("ActionLog.deleteEmployee.start");
        var employee = employeeRepository.findByIdAndEmployeeStatus(id, ExistStatus.ACTIVE.getId())
                .orElseThrow(() -> new NotFoundException("Employee is not found for this id!"));
        employee.setEmployeeStatus(ExistStatus.DEACTIVE.getId());
        employeeRepository.save(employee);
        logger.info("ActionLog.deleteEmployee.end employee deleted");
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        logger.info("ActionLog.getAllEmployees.start");
        var allEmployees = employeeRepository.findAllByEmployeeStatus(ExistStatus.ACTIVE.getId());
        var employeeResponses = EmployeeMapper.INSTANCE.entityListToDtoList(allEmployees);
        logger.info("ActionLog.getAllEmployees.end employee count : {}",employeeResponses.size());
        return employeeResponses;
    }
}
