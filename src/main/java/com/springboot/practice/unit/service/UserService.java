package com.springboot.practice.unit.service;

import com.springboot.practice.dto.UserDTO;

public interface UserService {

    UserDTO getUserByUsername(String username);

    UserDTO createUser(UserDTO userDTO);
}
