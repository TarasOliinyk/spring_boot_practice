package com.springboot.practice.controller;

import com.springboot.practice.dto.PermissionDTO;
import com.springboot.practice.service.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasAuthority('WRITE_USER')")
@RestController
@RequestMapping("/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permission")
    public ResponseEntity<PermissionDTO> createPermission(@RequestBody PermissionDTO permissionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionService.createPermission(permissionDTO));
    }

    @GetMapping("/permission/{id}")
    public ResponseEntity<PermissionDTO> getPermission(@PathVariable (name = "id") Integer id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(permissionService.getPermission(id));
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.FOUND)
    public List<PermissionDTO> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    @PutMapping("/permission")
    public ResponseEntity<PermissionDTO> updatePermission(@RequestBody PermissionDTO permissionDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.updatePermission(permissionDTO));
    }

    @DeleteMapping("/permission/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deletePermission(@PathVariable (name = "id") Integer id) {
        permissionService.deletePermission(id);
    }
}
