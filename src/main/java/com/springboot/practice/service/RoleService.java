package com.springboot.practice.service;

import com.springboot.practice.dto.RoleDTO;

import java.util.List;

public interface RoleService {

    RoleDTO createRole(RoleDTO roleDTO);

    RoleDTO getRole(Integer roleId);

    List<RoleDTO> getAllRoles();

    List<RoleDTO> getRolesWithIds(List<Integer> roleIds);

    RoleDTO updateRole(RoleDTO roleDTO);

    void deleteRole(Integer roleId);
}
