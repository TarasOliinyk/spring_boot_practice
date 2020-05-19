package com.springboot.practice.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class PermissionDTO {

    private Integer id;

    private String name;

    // ToDo: Should get rid of this field. Permission should be linked to Role via OneToMany relation without a hard
    //  dependency on Role (it should be possible to assign same Permission to multiple Roles)
    private Integer roleId;

    public PermissionDTO(String name, Integer roleId) {
        this.name = name;
        this.roleId = roleId;
    }
}
