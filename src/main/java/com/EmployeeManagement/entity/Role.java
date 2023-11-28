package com.EmployeeManagement.entity;

import com.EmployeeManagement.model.ExistStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @Column(name = "role_status")
    private int roleStatus;

    @PrePersist
    private void defaultSet(){
        roleStatus= ExistStatus.ACTIVE.getId();
    }
}
