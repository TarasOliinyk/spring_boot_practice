package com.springboot.practice.service;

import com.springboot.practice.dto.RoleDTO;

import java.util.List;

public interface RoleService {

    RoleDTO createRole(RoleDTO roleDTO);

    RoleDTO getRoleById(Integer roleId);

    RoleDTO getRoleByName(String roleName);

    List<RoleDTO> getAllRoles();

    List<RoleDTO> getRolesWithIds(List<Integer> roleIds);

    RoleDTO updateRole(RoleDTO roleDTO);

    RoleDTO addPermissions(Integer roleId, List<Integer> permissionIds);

    RoleDTO removePermissions(Integer roleId, List<Integer> permissionIds);

    void deleteRole(Integer roleId);
}
