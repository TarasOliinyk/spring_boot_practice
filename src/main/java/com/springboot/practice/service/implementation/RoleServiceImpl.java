package com.springboot.practice.service.implementation;

import com.springboot.practice.dto.PermissionDTO;
import com.springboot.practice.dto.RoleDTO;
import com.springboot.practice.exceptions.role.RoleNotFoundException;
import com.springboot.practice.model.Role;
import com.springboot.practice.repository.PermissionRepository;
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
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;

    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        logger.info("Create role " + roleDTO.getName());
        Role role = modelMapper.map(roleDTO, Role.class);
        return modelMapper.map(roleRepository.save(role), RoleDTO.class);
    }

    @Override
    public RoleDTO getRoleById(Integer roleId) {
        logger.info("Get role with id " + roleId);
        return modelMapper.map(roleRepository.findOneById(roleId).orElseThrow(
                () -> new RoleNotFoundException("There is no role with id " + roleId)), RoleDTO.class);
    }

    @Override
    public RoleDTO getRoleByName(String roleName) {
        logger.info("Get role with name " + roleName);
        return modelMapper.map(roleRepository.findOneByName(roleName).orElseThrow(
                () -> new RoleNotFoundException("There is no role with name " + roleName)), RoleDTO.class);
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
        Integer roleId = roleDTO.getId();
        logger.info(String.format("Update role with id %s, updated role: %s", roleId, roleDTO.toString()));
        Role role = roleRepository.findOneById(roleId).orElse(null);

        if (null != role) {
            role = modelMapper.map(roleDTO, Role.class);
            return modelMapper.map(roleRepository.save(role), RoleDTO.class);
        } else {
            throw new RoleNotFoundException("Role cannot be updated. There is no role with id " + roleId);
        }
    }

    private RoleDTO manipulateRolePermissions(Integer roleId, List<Integer> permissionIds, boolean add) {
        RoleDTO roleDTO = getRoleById(roleId);
        Type listType = new TypeToken<List<PermissionDTO>>(){}.getType();
        List<PermissionDTO> permissionDTOs = modelMapper.map(permissionRepository.findAllByIdIn(permissionIds), listType);

        if (add) {
            roleDTO.getPermissions().addAll(permissionDTOs);
        } else {
            roleDTO.getPermissions().removeAll(permissionDTOs);
        }
        return updateRole(roleDTO);
    }

    @Override
    public RoleDTO addPermissions(Integer roleId, List<Integer> permissionIds) {
        return manipulateRolePermissions(roleId, permissionIds, true);
    }

    @Override
    public RoleDTO removePermissions(Integer roleId, List<Integer> permissionIds) {
        return manipulateRolePermissions(roleId, permissionIds, false);
    }

    @Override
    public void deleteRole(Integer roleId) {
        logger.info("Delete role with id " + roleId);
        Role role = roleRepository.findOneById(roleId).orElse(null);

        if (null != role) {
            roleRepository.deleteById(roleId);
        } else {
            throw new RoleNotFoundException("Role cannot be deleted. There is no role with id " + roleId);
        }
    }
}
