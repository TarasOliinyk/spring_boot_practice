package com.springboot.practice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "password")
@ToString(exclude = "password")
public class UserDTO {

    public Integer id;

    public String username;

    public String password;
}
