package com.springboot.practice.service.implementation;

import com.springboot.practice.dto.PermissionDTO;
import com.springboot.practice.exceptions.permission.PermissionNotFoundException;
import com.springboot.practice.model.Permission;
import com.springboot.practice.repository.PermissionRepository;
import com.springboot.practice.service.PermissionService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    private static Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;

    public PermissionServiceImpl(PermissionRepository permissionRepository, ModelMapper modelMapper) {
        this.permissionRepository = permissionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PermissionDTO createPermission(PermissionDTO permissionDTO) {
        logger.info("Create permission " + permissionDTO.getName());
        Permission permission = modelMapper.map(permissionDTO, Permission.class);
        return modelMapper.map(permissionRepository.save(permission), PermissionDTO.class);
    }

    @Override
    public PermissionDTO getPermission(Integer permissionId) {
        logger.info("Get permission with id " + permissionId);
        return modelMapper.map(permissionRepository.findOneById(permissionId).orElseThrow(() ->
                new PermissionNotFoundException("There is no permission with id " + permissionId)), PermissionDTO.class);
    }

    @Override
    public List<PermissionDTO> getAllPermissions() {
        logger.info("Get all permissions");
        Type listType = new TypeToken<List<PermissionDTO>>(){}.getType();
        return modelMapper.map(permissionRepository.findAll(), listType);
    }

    @Override
    public PermissionDTO updatePermission(PermissionDTO permissionDTO) {
        logger.info(String.format("Update permission with id %s, updated permission %s", permissionDTO.getId(),
                permissionDTO.toString()));
        Permission permission = modelMapper.map(permissionDTO, Permission.class);
        return modelMapper.map(permissionRepository.save(permission), PermissionDTO.class);
    }

    @Override
    public void deletePermission(Integer permissionId) {
        logger.info("Delete permission with id " + permissionId);
        permissionRepository.deleteById(permissionId);
    }
}
