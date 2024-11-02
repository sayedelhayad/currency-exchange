package com.ce.auth.config;

import com.ce.auth.repository.UserEntity;
import com.ce.auth.repository.UserRepository;
import com.ce.bill.dto.UserType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
class UserInitTestDataCreator {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() throws ParseException {

        SimpleDateFormat smp = new SimpleDateFormat("yyyy-MM-dd");
        Date joiningDate = smp.parse("2020-05-01");

        String encryptedPassword = new BCryptPasswordEncoder().encode("123");

        userRepository.save(new UserEntity("emp", encryptedPassword, UserType.EMPLOYEE, joiningDate));
        userRepository.save(new UserEntity("cus", encryptedPassword, UserType.CUSTOMER, joiningDate));

    }
}