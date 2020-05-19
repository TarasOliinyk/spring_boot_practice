package com.springboot.practice.service.implementation;

import com.springboot.practice.dto.UserDTO;
import com.springboot.practice.exceptions.user.UserNotFoundException;
import com.springboot.practice.model.Role;
import com.springboot.practice.model.User;
import com.springboot.practice.repository.UserRepository;
import com.springboot.practice.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

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
        Type listType = new TypeToken<List<Role>>(){}.getType();
        user.setRoles(modelMapper.map(userDTO.getRoles(), listType));
        return modelMapper.map(userRepository.save(user), UserDTO.class);
    }

    @Override
    public UserDTO getUserById(Integer userId) {
        logger.info("Get user with id " + userId);
        return modelMapper.map(userRepository.findOneById(userId).orElseThrow(
                () -> new UserNotFoundException("There is no user with id " + userId)), UserDTO.class);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        logger.info("Get all users");
        Type listType = new TypeToken<List<UserDTO>>(){}.getType();
        return modelMapper.map(userRepository.findAll(), listType);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        logger.info("Get user with username " + username);
        return modelMapper.map(userRepository.findOneByUsername(username).orElseThrow(
                () -> new UserNotFoundException("There is no user with username " + username)), UserDTO.class);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        logger.info(String.format("Update user with id %s, updated user: %s", userDTO.getId(), userDTO.toString()));
        User user = modelMapper.map(userDTO, User.class);
        return modelMapper.map(userRepository.save(user), UserDTO.class);
    }
}
