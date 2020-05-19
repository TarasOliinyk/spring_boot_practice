package com.springboot.practice.service;

import com.springboot.practice.dto.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserById(Integer userId);

    List<UserDTO> getAllUsers();

    UserDTO getUserByUsername(String username);

    UserDTO updateUser(UserDTO userDTO);
}
