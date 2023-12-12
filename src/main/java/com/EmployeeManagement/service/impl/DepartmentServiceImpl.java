package com.EmployeeManagement.service.impl;

import com.EmployeeManagement.dto.request.DepartmentRequest;
import com.EmployeeManagement.dto.response.DepartmentResponse;
import com.EmployeeManagement.entity.Department;
import com.EmployeeManagement.exception.NotFoundException;
import com.EmployeeManagement.mapper.DepartmentMapper;
import com.EmployeeManagement.model.ExistStatus;
import com.EmployeeManagement.repository.DepartmentRepository;
import com.EmployeeManagement.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private static final Logger logger= LoggerFactory.getLogger(DepartmentServiceImpl.class);
    @Override
    public Optional<DepartmentResponse> saveDepartment(DepartmentRequest request) {
        logger.info("ActionLog.saveDepartment.start request: {}",request);
        var department = DepartmentMapper.INSTANCE.dtoToEntity(request);
        var savedDepartment = departmentRepository.save(department);
        var response=DepartmentMapper.INSTANCE.entityToDto(savedDepartment);
        logger.info("ActionLog.saveDepartment.stop response: {}",response);
        return Optional.of(response);
    }

    @Override
    public Optional<DepartmentResponse> getDepartment(int id) {
        logger.info("ActionLog.getDepartment.start id:{}",id);
        var department = departmentRepository.findByIdAndDepartmentStatus(id, ExistStatus.ACTIVE.getId())
                .orElseThrow(() -> new NotFoundException("Department is Not Found for this Id"));
        var departmentResponse = DepartmentMapper.INSTANCE.entityToDto(department);
        logger.info("ActionLog.getDepartment.end id:{}",id);
        return Optional.of(departmentResponse);
    }

    @Override
    public Department getDepartmentById(int id) {
        logger.info("ActionLog.getDepartmentById.start id:{}",id);
        var department = departmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Department is not found for this id!"));
        logger.info("ActionLog.getDepartmentById.end id:{}",id);
        return department;
    }

    @Override
    public Optional<DepartmentResponse> updateDepartment(int id, DepartmentRequest request) {
        logger.info("ActionLog.updateDepartment.start id: {}",id);
        var department = departmentRepository.findByIdAndDepartmentStatus(id, ExistStatus.ACTIVE.getId())
                .orElseThrow(() -> new NotFoundException("Department is Not Found for this Id"));
        DepartmentMapper.INSTANCE.dtoToEntity(department,request);
        var updatedDepartment = departmentRepository.save(department);
        var response = DepartmentMapper.INSTANCE.entityToDto(updatedDepartment);
        logger.info("ActionLog.updateDepartment.end id: {}",id);
        return Optional.of(response);
    }

    @Override
    public void deleteDepartment(int id) {
        logger.info("ActionLog.deleteDepartment.start");
        var department = departmentRepository.findByIdAndDepartmentStatus(id, ExistStatus.ACTIVE.getId())
                .orElseThrow(() -> new NotFoundException("Department is Not Found for this Id"));
        department.setDepartmentStatus(ExistStatus.DEACTIVE.getId());
        departmentRepository.save(department);
        departmentRepository.deactivePositionByDepartmentId(id);
        logger.info("ActionLog.deleteDepartment.end department deleted");
    }

    @Override
    public List<DepartmentResponse> getAllDepartments() {
        logger.info("ActionLog.getAllDepartments.start");
        var allDepartments = departmentRepository.findAllByDepartmentStatus(ExistStatus.ACTIVE.getId());
        var departmentResponses = DepartmentMapper.INSTANCE.entityListToDtoList(allDepartments);
        logger.info("ActionLog.getAllDepartments.end departments count : {}",departmentResponses.size());
        return departmentResponses;
    }
}
