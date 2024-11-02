package com.ce.auth.dto;

import com.ce.bill.dto.UserType;

import java.util.Date;

public record SignUpDto(
        String username,
        String password,
        Date joiningDate,
        UserType type) {
}