package com.springboot.practice.dto;

import lombok.*;

@Data
@EqualsAndHashCode(exclude = "password")
@ToString(exclude = "password")
public class UserDTO {

    public Integer id;

    public String username;

    public String password;
}
