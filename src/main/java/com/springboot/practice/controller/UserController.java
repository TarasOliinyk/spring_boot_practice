package com.springboot.practice.controller;

import com.springboot.practice.dto.RoleDTO;
import com.springboot.practice.dto.UserDTO;
import com.springboot.practice.service.RoleService;
import com.springboot.practice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@PreAuthorize("hasAuthority('WRITE_USER')")
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDTO> singUp(@RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable (name = "id") Integer id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(userService.getUserById(id));
    }

    @GetMapping("/list")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> assignRole(@PathVariable (name = "userId") Integer userId,
                                              @RequestBody List<Integer> roleIds) {
        List<RoleDTO> roleDTOs = roleService.getRolesWithIds(roleIds).stream().peek(roleDTO -> roleDTO.setUserId(userId))
                .map(roleService::updateRole).collect(Collectors.toList());
        UserDTO userDTO = userService.getUserById(userId);
        userDTO.setRoles(roleDTOs);
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userDTO));
    }
}
