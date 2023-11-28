package com.EmployeeManagement.service.impl;

import com.EmployeeManagement.dto.request.EmployeeRequest;
import com.EmployeeManagement.dto.response.EmployeeResponse;
import com.EmployeeManagement.exception.NotFoundException;
import com.EmployeeManagement.mapper.EmployeeMapper;
import com.EmployeeManagement.model.ExistStatus;
import com.EmployeeManagement.repository.EmployeeRepository;
import com.EmployeeManagement.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger logger= LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeResponse saveEmployee(EmployeeRequest request) {
        logger.info("ActionLog.saveEmployee.start request: {}",request);
        var employee = EmployeeMapper.INSTANCE.dtoToEntity(request);
        var savedEmployee = employeeRepository.save(employee);
        var response= EmployeeMapper.INSTANCE.entityToDto(savedEmployee);
        logger.info("ActionLog.saveEmployee.stop response: {}",response);
        return response;

    }

    @Override
    public EmployeeResponse getEmployee(int id) {
        logger.info("ActionLog.getEmployee.start id:{}",id);
        var employee = employeeRepository.findByIdAndEmployeeStatus(id, ExistStatus.ACTIVE.getId())
                .orElseThrow(() -> new NotFoundException("Employee is not found for this id!"));
        var response= EmployeeMapper.INSTANCE.entityToDto(employee);
        logger.info("ActionLog.getEmployee.end id:{}",id);
        return response;
    }

    @Override
    public EmployeeResponse updateEmployee(int id, EmployeeRequest request) {
        logger.info("ActionLog.updateEmployee.start id: {}",id);
        var employee = employeeRepository.findByIdAndEmployeeStatus(id, ExistStatus.ACTIVE.getId())
                .orElseThrow(() -> new NotFoundException("Employee is not found for this id!"));
        EmployeeMapper.INSTANCE.dtoToEntity(employee,request);
        var updatedEmployee = employeeRepository.save(employee);
        var response =EmployeeMapper.INSTANCE.entityToDto(updatedEmployee);
        logger.info("ActionLog.updateEmployee.end id: {}",id);
        return response;
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
