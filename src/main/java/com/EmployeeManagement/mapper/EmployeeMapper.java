package com.EmployeeManagement.mapper;

import com.EmployeeManagement.dto.request.EmployeeRequest;
import com.EmployeeManagement.dto.response.EmployeeResponse;
import com.EmployeeManagement.entity.Employee;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
        ,unmappedTargetPolicy = ReportingPolicy.IGNORE
        ,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class EmployeeMapper {

    public static final EmployeeMapper INSTANCE= Mappers.getMapper(EmployeeMapper.class);

    @Mapping(target = "department.id",source = "departmentId")
    @Mapping(target = "position.id",source = "positionId")
    public abstract Employee dtoToEntity(EmployeeRequest request);

    @Mapping(target = "department.id",source = "departmentId")
    @Mapping(target = "position.id",source = "positionId")
    public abstract void dtoToEntity(@MappingTarget Employee employee, EmployeeRequest request);
    @Mapping(target = "departmentId",source = "department.id")
    @Mapping(target = "positionId",source = "position.id")
    public abstract EmployeeResponse entityToDto(Employee employee);

    @Mapping(target = "departmentId",source = "department.id")
    @Mapping(target = "positionId",source = "position.id")
    public abstract List<EmployeeResponse> entityListToDtoList(List<Employee> employees);
}
