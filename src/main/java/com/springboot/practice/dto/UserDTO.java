package com.springboot.practice.dto;

import com.springboot.practice.data.Role;
import lombok.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "password")
@ToString(exclude = "password")
public class UserDTO {

    public Integer id;

    public String username;

    public String password;

    public Role role = Role.ROLE_USER;
}
