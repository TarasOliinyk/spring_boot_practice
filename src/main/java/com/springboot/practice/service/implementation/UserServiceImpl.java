package com.springboot.practice.service.implementation;

import com.springboot.practice.data.UserRole;
import com.springboot.practice.dto.RoleDTO;
import com.springboot.practice.dto.user.UserCreateDTO;
import com.springboot.practice.dto.user.UserDTO;
import com.springboot.practice.exceptions.role.RoleNotFoundException;
import com.springboot.practice.exceptions.user.UserNotFoundException;
import com.springboot.practice.model.Role;
import com.springboot.practice.model.User;
import com.springboot.practice.repository.RoleRepository;
import com.springboot.practice.repository.UserRepository;
import com.springboot.practice.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserCreateDTO createUser(UserCreateDTO userCreateDTO) {
        logger.info("Create user with username " + userCreateDTO.getUsername());
        User user = modelMapper.map(userCreateDTO, User.class);
        user.setPassword(bCryptPasswordEncoder.encode(userCreateDTO.getPassword()));
        Role role = roleRepository.findOneByName(UserRole.Name.USER).orElseThrow(() -> new RoleNotFoundException(
                "There is no role wth name " + UserRole.Name.USER));
        user.getRoles().add(role);
        return modelMapper.map(userRepository.save(user), UserCreateDTO.class);
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
    @Transactional
    public UserDTO getUserByUsername(String username) {
        logger.info("Get user with username " + username);
        return modelMapper.map(userRepository.findOneByUsername(username).orElseThrow(
                () -> new UserNotFoundException("There is no user with username " + username)), UserDTO.class);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        Integer userId = userDTO.getId();
        logger.info(String.format("Update user with id %s, updated user: %s", userId, userDTO.toString()));
        User user = userRepository.findOneById(userId).orElse(null);

        if (null != user) {
            user = modelMapper.map(userDTO, User.class);
            return modelMapper.map(userRepository.save(user), UserDTO.class);
        } else {
            throw new UserNotFoundException("User cannot be updated. There is no user with id " + userId);
        }
    }

    @Override
    public UserDTO assignRoles(Integer userId, List<Integer> roleIds) {
        UserDTO userDTO = getUserById(userId);
        Type listType = new TypeToken<List<RoleDTO>>(){}.getType();
        List<RoleDTO> roleDTOs = modelMapper.map(roleRepository.findAllByIdIn(roleIds), listType);
        userDTO.getRoles().addAll(roleDTOs);
        return updateUser(userDTO);
    }

    @Override
    public UserDTO unassignRole(Integer userId, Integer roleId) {
        UserDTO userDTO = getUserById(userId);
        RoleDTO roleToRemove = modelMapper.map(roleRepository.findOneById(roleId).orElseThrow(
                () -> new RoleNotFoundException("There is no role with id " + roleId)), RoleDTO.class);
        userDTO.getRoles().remove(roleToRemove);
        return updateUser(userDTO);
    }

    @Override
    @Transactional
    public List<SimpleGrantedAuthority> getUserAuthorities(Integer userId) {
        logger.info("Get authorities of user with id " + userId);
        UserDTO userDTO = getUserById(userId);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        userDTO.getRoles().stream().peek(roleDTO -> authorities.add(new SimpleGrantedAuthority(roleDTO.getName())))
                .map(RoleDTO::getPermissions).flatMap(Collection::stream)
                .forEach(permissionDTO -> authorities.add(new SimpleGrantedAuthority(permissionDTO.getName())));
        return authorities;
    }

    @Override
    public void deleteUser(Integer userId) {
        logger.info("Delete user with id " + userId);
        User user = userRepository.findOneById(userId).orElse(null);

        if (null != user) {
            userRepository.deleteById(userId);
        } else {
            throw new UserNotFoundException("User cannot be deleted. There is no user with id " + userId);
        }
    }
}
