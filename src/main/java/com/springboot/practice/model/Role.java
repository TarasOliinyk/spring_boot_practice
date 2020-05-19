package com.springboot.practice.model;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column
    private Integer userId;

    @OneToMany(fetch = FetchType.EAGER ,mappedBy = "roleId")
    @Fetch(FetchMode.SELECT)
    private List<Permission> permissions = new ArrayList<>();
}
