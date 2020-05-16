package com.springboot.practice.unit.service.implementation;

import com.springboot.practice.dto.UserDTO;
import com.springboot.practice.exceptions.user.UserNotFoundException;
import com.springboot.practice.model.User;
import com.springboot.practice.repository.UserRepository;
import com.springboot.practice.unit.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        logger.info("Create user with username " + userDTO.getUsername());
        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        return modelMapper.map(userRepository.save(user), UserDTO.class);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        logger.info("Get user with username " + username);
        return modelMapper.map(userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("There is no user with username " + username)), UserDTO.class);
    }
}
