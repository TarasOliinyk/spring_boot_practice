package com.springboot.practice.unit.service;

import com.springboot.practice.dto.UserDTO;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserByUsername(String username);
}
