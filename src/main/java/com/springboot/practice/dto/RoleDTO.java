package com.springboot.practice.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RoleDTO {

    private Integer id;

    private String name;

    // ToDo: Should get rid of this field. Role should be linked to User via OneToMany relation without a hard dependency
    //  on User (it should be possible to assign same Role to multiple Users)
    private Integer userId;

    private List<PermissionDTO> permissions = new ArrayList<>();
}
