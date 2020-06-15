package com.springboot.practice.service;

import com.springboot.practice.dto.user.UserCreateDTO;
import com.springboot.practice.dto.user.UserDTO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public interface UserService {

    UserCreateDTO createUser(UserCreateDTO userDTO);

    UserDTO getUserById(Integer userId);

    List<UserDTO> getAllUsers();

    UserDTO getUserByUsername(String username);

    UserDTO updateUser(UserDTO userDTO);

    UserDTO assignRoles(Integer userId, List<Integer> roleIds);

    UserDTO unassignRole(Integer userId, Integer roleId);

    List<SimpleGrantedAuthority> getUserAuthorities(Integer userId);

    void deleteUser(Integer userId);
}
