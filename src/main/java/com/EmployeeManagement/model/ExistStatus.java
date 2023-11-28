package com.EmployeeManagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
public enum ExistStatus {
    DEACTIVE(0,false),
    ACTIVE(1,true);

    @Getter
    private final int id;

    @Getter
    private final boolean userStatus;
}
