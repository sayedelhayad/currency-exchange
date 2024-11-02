package com.ce.auth.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    UserDetails getUserByUsername(String username) throws Exception;

}
