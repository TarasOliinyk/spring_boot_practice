package com.springboot.practice.controller;

import com.springboot.practice.data.UserRole;
import com.springboot.practice.dto.RoleDTO;
import com.springboot.practice.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@PreAuthorize("hasAuthority('" + UserRole.Name.ADMIN + "')")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/role")
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(roleDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRole(@PathVariable (name = "id") Integer id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(roleService.getRoleById(id));
    }

    @GetMapping("/list")
    public List<RoleDTO> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PutMapping("/role")
    public ResponseEntity<RoleDTO> updateRole(@RequestBody RoleDTO roleDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.updateRole(roleDTO));
    }

    @PutMapping("/{id}/addPermissions")
    public ResponseEntity<RoleDTO> addPermissionsToRole(@PathVariable (name = "id") Integer roleId,
                                                        @RequestBody List<Integer> permissionIds) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.addPermissions(roleId, permissionIds));
    }

    @PutMapping("/{id}/removePermissions")
    public ResponseEntity<RoleDTO> removePermissionsFromRole(@PathVariable (name = "id") Integer roleId,
                                                             @RequestBody List<Integer> permissionIds) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.removePermissions(roleId, permissionIds));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteRole(@PathVariable (name = "id") Integer id) {
        roleService.deleteRole(id);
    }
}
