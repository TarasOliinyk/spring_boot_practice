package com.springboot.practice.service;

import com.springboot.practice.dto.PermissionDTO;

import java.util.List;

public interface PermissionService {

    PermissionDTO createPermission(PermissionDTO permissionDTO);

    PermissionDTO getPermission(Integer permissionId);

    List<PermissionDTO> getAllPermissions();

    PermissionDTO updatePermission(PermissionDTO permissionDTO);

    void deletePermission(Integer permissionId);
}
