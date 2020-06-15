package com.springboot.practice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboot.practice.dto.user.UserDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"users", "permissions"})
@ToString(exclude = {"users", "permissions"})
public class RoleDTO {

    private Integer id;

    private String name;

    @JsonIgnore
    private List<UserDTO> users = new ArrayList<>();

    private List<PermissionDTO> permissions = new ArrayList<>();
}
