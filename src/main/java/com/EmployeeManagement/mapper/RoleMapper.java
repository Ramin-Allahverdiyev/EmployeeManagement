package com.EmployeeManagement.mapper;

import com.EmployeeManagement.dto.request.RoleRequest;
import com.EmployeeManagement.dto.response.RoleResponse;
import com.EmployeeManagement.entity.Role;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
        ,unmappedTargetPolicy = ReportingPolicy.IGNORE
        ,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class RoleMapper {
    public static final RoleMapper INSTANCE= Mappers.getMapper(RoleMapper.class);

    public abstract Role dtoToEntity(RoleRequest request);

    public abstract RoleResponse entityToDto(Role role);

    public abstract void dtoToEntity(@MappingTarget Role role, RoleRequest request);

    public abstract List<RoleResponse> entityListToDtoList(List<Role> lessons);
}
