package com.springboot.practice.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"users", "permissions"})
@ToString(exclude = {"users", "permissions"})
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<>();

    @ManyToMany
    private List<Permission> permissions = new ArrayList<>();

    public Role(String name) {
        this.name = name;
    }
}
