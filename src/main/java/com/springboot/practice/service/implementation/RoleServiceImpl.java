package com.springboot.practice.service.implementation;

import com.springboot.practice.dto.RoleDTO;
import com.springboot.practice.exceptions.role.RoleNotFoundException;
import com.springboot.practice.model.Role;
import com.springboot.practice.repository.RoleRepository;
import com.springboot.practice.service.RoleService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private static Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        logger.info("Create role " + roleDTO.getName());
        Role role = modelMapper.map(roleDTO, Role.class);
        return modelMapper.map(roleRepository.save(role), RoleDTO.class);
    }

    @Override
    public RoleDTO getRole(Integer roleId) {
        logger.info("Get role with id " + roleId);
        return modelMapper.map(roleRepository.findOneById(roleId).orElseThrow(
                () -> new RoleNotFoundException("There is no role with id " + roleId)), RoleDTO.class);
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        logger.info("Get all roles");
        Type listType = new TypeToken<List<RoleDTO>>(){}.getType();
        return modelMapper.map(roleRepository.findAll(), listType);
    }

    @Override
    public List<RoleDTO> getRolesWithIds(List<Integer> roleIds) {
        logger.info("Get roles with ids " + roleIds.toString());
        Type listType = new TypeToken<List<RoleDTO>>(){}.getType();
        return modelMapper.map(roleRepository.findAllByIdIn(roleIds), listType);
    }

    @Override
    public RoleDTO updateRole(RoleDTO roleDTO) {
        logger.info(String.format("Update role with id %s, updated role: %s", roleDTO.getId(), roleDTO.toString()));
        Role role = modelMapper.map(roleDTO, Role.class);
        return modelMapper.map(roleRepository.save(role), RoleDTO.class);
    }

    @Override
    public void deleteRole(Integer roleId) {
        logger.info("Delete role with id " + roleId);
        roleRepository.deleteById(roleId);
    }
}
