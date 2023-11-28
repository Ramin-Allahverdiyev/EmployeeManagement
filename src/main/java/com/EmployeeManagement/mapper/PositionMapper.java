package com.EmployeeManagement.mapper;

import com.EmployeeManagement.dto.request.PositionRequest;
import com.EmployeeManagement.dto.response.PositionResponse;
import com.EmployeeManagement.entity.Position;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
        ,unmappedTargetPolicy = ReportingPolicy.IGNORE
        ,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class PositionMapper {
    public static final PositionMapper INSTANCE= Mappers.getMapper(PositionMapper.class);

    @Mapping(target ="department.id" ,source ="departmentId")
    public abstract Position dtoToEntity(PositionRequest request);
    @Mapping(target ="department.id" ,source ="departmentId")
    public abstract void dtoToEntity(@MappingTarget Position position, PositionRequest request);
    @Mapping(target ="departmentId" ,source ="department.id")
    public abstract PositionResponse entityToDto(Position position);
    @Mapping(target ="departmentId" ,source ="department.id")
    public abstract List<PositionResponse> entityListToDtoList(List<Position> positions);
}
