package com.ce.auth.dto;

import com.ce.bill.dto.UserType;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class User {

    private String username;

    private String password;

    private UserType type;

    private Date joiningDate;

    private int tenure;

}