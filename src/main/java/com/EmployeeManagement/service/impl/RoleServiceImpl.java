package com.EmployeeManagement.service.impl;

import com.EmployeeManagement.dto.request.RoleRequest;
import com.EmployeeManagement.dto.response.RoleResponse;
import com.EmployeeManagement.exception.NotFoundException;
import com.EmployeeManagement.mapper.RoleMapper;
import com.EmployeeManagement.model.ExistStatus;
import com.EmployeeManagement.repository.RoleRepository;
import com.EmployeeManagement.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private static final Logger logger= LoggerFactory.getLogger(RoleServiceImpl.class);
    private final RoleRepository roleRepository;

    @Override
    public RoleResponse saveRole(RoleRequest request) {
        logger.info("ActionLog.saveRole.start request: {}",request);
        var role = RoleMapper.INSTANCE.dtoToEntity(request);
        var savedRole = roleRepository.save(role);
        var response =RoleMapper.INSTANCE.entityToDto(savedRole);
        logger.info("ActionLog.saveRole.stop response: {}",response);
        return response;
    }

    @Override
    public RoleResponse updateRole(int id, RoleRequest request) {
        logger.info("ActionLog.updateRole.start id: {}",id);
        var role = roleRepository.findByIdAndRoleStatus(id, ExistStatus.ACTIVE.getId())
                .orElseThrow(() -> new NotFoundException("Role is not found"));
        RoleMapper.INSTANCE.dtoToEntity(role,request);
        var updatedRole = roleRepository.save(role);
        var roleResponse = RoleMapper.INSTANCE.entityToDto(updatedRole);
        logger.info("ActionLog.updateRole.end id: {}",id);
        return roleResponse;
    }

    @Override
    public void deleteRole(int id) {
        logger.info("ActionLog.deleteRole.start");
        var role = roleRepository.findByIdAndRoleStatus(id, ExistStatus.ACTIVE.getId())
                .orElseThrow(() -> new NotFoundException("Role is not found"));
        role.setRoleStatus(ExistStatus.DEACTIVE.getId());
        roleRepository.save(role);
        logger.info("ActionLog.deleteRole.end role deleted");
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        logger.info("ActionLog.getAllRoles.start");
        var allRoles = roleRepository.findAllByRoleStatus(ExistStatus.ACTIVE.getId());
        var roleResponses = RoleMapper.INSTANCE.entityListToDtoList(allRoles);
        logger.info("ActionLog.getAllRoles.end roles count : {}",roleResponses.size());
        return roleResponses;
    }
}
