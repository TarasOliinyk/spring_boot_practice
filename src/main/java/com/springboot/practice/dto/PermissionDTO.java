package com.springboot.practice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "roles")
@ToString(exclude = "roles")
public class PermissionDTO {

    private Integer id;

    private String name;

    @JsonIgnore
    private List<RoleDTO> roles = new ArrayList<>();
}
