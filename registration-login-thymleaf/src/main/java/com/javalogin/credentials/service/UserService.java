package com.javalogin.credentials.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.javalogin.credentials.model.User;
import com.javalogin.credentials.web.dto.UserRegistrationDto;

public interface UserService extends UserDetailsService {
	User save(UserRegistrationDto registrationDto);
}
