package com.example.convenientparking.Constants;

import lombok.AllArgsConstructor;
@AllArgsConstructor
public enum RoleEnum {
    ADMIN(1),
    EMPLOYEE(2),
    USER(3);
    public final long value;
}
