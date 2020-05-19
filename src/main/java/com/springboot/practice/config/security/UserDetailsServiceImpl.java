package com.springboot.practice.config.security;

import com.springboot.practice.dto.PermissionDTO;
import com.springboot.practice.dto.RoleDTO;
import com.springboot.practice.dto.UserDTO;
import com.springboot.practice.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userDTO = userService.getUserByUsername(username);
        List<SimpleGrantedAuthority> userPermissions = userDTO.getRoles().stream().map(RoleDTO::getPermissions)
                .flatMap(Collection::stream).map(PermissionDTO::getName).map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new User(userDTO.getUsername(), userDTO.getPassword(), userPermissions);
    }
}
