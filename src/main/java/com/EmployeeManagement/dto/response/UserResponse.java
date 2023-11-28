package com.EmployeeManagement.dto.response;

import com.EmployeeManagement.dto.RoleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private int id;
    private String name;
    private String surname;
    private String email;
    private String username;
    private String password;
    private Set<RoleDto> roles;
}
