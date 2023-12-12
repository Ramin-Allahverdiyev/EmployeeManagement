package com.EmployeeManagement.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionRequest {
    @NotEmpty(message = "Position name cannot be empty")
    private String name;
    @Min(message = "Salary cannot be less than 350 AZN", value = 350L)
    private double salary;
    @Min(value = 1, message = "Select a valid Position!")
    private int departmentId;
}
