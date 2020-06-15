package com.springboot.practice.controller;

import com.springboot.practice.annotation.permission.user.*;
import com.springboot.practice.annotation.role.IsAdmin;
import com.springboot.practice.dto.user.UserCreateDTO;
import com.springboot.practice.dto.user.UserDTO;
import com.springboot.practice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserCreateDTO> singUp(@RequestBody UserCreateDTO userCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userCreateDTO));
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@RequestBody @Valid UserCreateDTO userCreateDTO) {
        // Login processing (JWT token generation) is handled by JWTAuthenticationFilter, this endpoint is created for
        // exposing it to Swagger
    }

    @GetMapping("/{id}")
    @HasUserReadPermission
    public ResponseEntity<UserDTO> getUser(@PathVariable (name = "id") Integer id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(userService.getUserById(id));
    }

    @GetMapping("/list")
    @IsAdmin
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{userId}/addRoles")
    @HasUserUpdatePermission
    public ResponseEntity<UserDTO> assignRoles(@PathVariable (name = "userId") Integer userId,
                                               @RequestBody List<Integer> roleIds) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.assignRoles(userId, roleIds));
    }

    @PutMapping("/{userId}/removeRole/{roleId}")
    @HasUserUpdatePermission
    public ResponseEntity<UserDTO> unassignRole(@PathVariable (name = "userId") Integer userId,
                                                @PathVariable (name = "roleId") Integer roleId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.unassignRole(userId, roleId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @HasUserDeletePermission
    public void deleteUser(@PathVariable (name = "id") Integer id) {
        userService.deleteUser(id);
    }
}
