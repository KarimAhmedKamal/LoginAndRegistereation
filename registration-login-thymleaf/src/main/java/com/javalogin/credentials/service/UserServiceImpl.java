package com.javalogin.credentials.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.javalogin.credentials.model.Role;
import com.javalogin.credentials.model.User;
import com.javalogin.credentials.repository.UserRepository;
import com.javalogin.credentials.web.dto.UserRegistrationDto;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;

	private BCryptPasswordEncoder passwordEncoder;

    @Autowired
	public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public User save(UserRegistrationDto registrationDto) {
		User user = new User(registrationDto.getFirstName(), registrationDto.getLastName(),
				registrationDto.getEmail(), passwordEncoder.encode(registrationDto.getPassword()), Arrays.asList(new Role("Role User")));
		return userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("invalid username or password!!");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles())); // null could be replaced with the passed roles

	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}
}
