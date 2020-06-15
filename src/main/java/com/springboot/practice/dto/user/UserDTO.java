package com.springboot.practice.dto.user;

import com.fasterxml.jackson.annotation.*;
import com.springboot.practice.dto.RoleDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"password", "roles"})
@ToString(exclude = {"password"})
public class UserDTO {

    private Integer id;

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonIgnore
    private List<RoleDTO> roles = new ArrayList<>();
}
