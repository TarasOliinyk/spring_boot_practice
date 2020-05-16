package com.springboot.practice.config.security;

import com.springboot.practice.dto.UserDTO;
import com.springboot.practice.unit.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userDTO = userService.getUserByUsername(username);
        return new User(userDTO.getUsername(), userDTO.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(userDTO.getRole().name())));
    }
}
