package com.springboot.practice.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "password")
@ToString(exclude = "password")
public class UserDTO {

    public Integer id;

    public String username;

    public String password;

    public List<RoleDTO> roles = new ArrayList<>();
}
