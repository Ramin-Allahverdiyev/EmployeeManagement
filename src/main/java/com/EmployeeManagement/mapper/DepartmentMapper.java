package com.EmployeeManagement.mapper;

import com.EmployeeManagement.dto.request.DepartmentRequest;
import com.EmployeeManagement.dto.response.DepartmentResponse;
import com.EmployeeManagement.entity.Department;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
        ,unmappedTargetPolicy = ReportingPolicy.IGNORE
        ,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class DepartmentMapper {
    public static final DepartmentMapper INSTANCE= Mappers.getMapper(DepartmentMapper.class);

    public abstract Department dtoToEntity(DepartmentRequest request);

    public abstract void dtoToEntity(@MappingTarget Department department, DepartmentRequest request);
    public abstract DepartmentResponse entityToDto(Department department);

    public abstract List<DepartmentResponse> entityListToDtoList(List<Department> departments);
}
