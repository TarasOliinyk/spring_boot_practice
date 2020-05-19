package com.springboot.practice.controller;

import com.springboot.practice.dto.PermissionDTO;
import com.springboot.practice.dto.RoleDTO;
import com.springboot.practice.service.PermissionService;
import com.springboot.practice.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@PreAuthorize("hasAuthority('WRITE_USER')")
@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;
    private final PermissionService permissionService;

    public RoleController(RoleService roleService, PermissionService permissionService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @PostMapping("/role")
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(roleDTO));
    }

    @GetMapping("/role/{id}")
    public ResponseEntity<RoleDTO> getRole(@PathVariable (name = "id") Integer id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(roleService.getRole(id));
    }

    @GetMapping("/list")
    public List<RoleDTO> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PutMapping("/role")
    public ResponseEntity<RoleDTO> updateRole(@RequestBody RoleDTO roleDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.updateRole(roleDTO));
    }

    @PutMapping("/role/{id}")
    public ResponseEntity<RoleDTO> addPermissionsToRole(@PathVariable (name = "id") Integer roleId,
                                                        @RequestBody List<String> permissions) {
        RoleDTO roleDTO = roleService.getRole(roleId);
        List<PermissionDTO> permissionDTOs = permissions.stream().map(permission -> new PermissionDTO(permission, roleId))
                .map(permissionService::createPermission).collect(Collectors.toList());
        roleDTO.setPermissions(permissionDTOs);
        return ResponseEntity.status(HttpStatus.OK).body(roleService.updateRole(roleDTO));
    }

    @DeleteMapping("/role/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteRole(@PathVariable (name = "id") Integer id) {
        roleService.deleteRole(id);
    }
}
