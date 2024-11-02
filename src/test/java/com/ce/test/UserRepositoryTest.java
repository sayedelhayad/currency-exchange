package com.ce.test;


import com.ce.auth.repository.UserEntity;
import com.ce.auth.repository.UserRepository;
import com.ce.bill.dto.UserType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    @DisplayName("Test 1:Save Employee Test")
    @Order(1)
    @Rollback(value = false)
    public void saveUserTest() throws ParseException {

        //Action
        String encryptedPassword = new BCryptPasswordEncoder().encode("123");
        SimpleDateFormat smp = new SimpleDateFormat("yyyy-MM-dd");
        Date joiningDate = smp.parse("2020-05-01");

        UserEntity userEntity = new UserEntity("testUser", encryptedPassword, UserType.EMPLOYEE, joiningDate);

        userRepository.save(userEntity);

        //Verify
        assert(userEntity.getId() > 0);
    }

    @Test
    @Order(2)
    public void getUserTest(){

        //Action
        UserEntity userEntity = userRepository.findById(1L).get();
        //Verify
        assert(userEntity.getId() == 1L);
    }

}
