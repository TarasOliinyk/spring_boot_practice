package com.springboot.practice.model;

import com.springboot.practice.data.Role;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "password")
@ToString(exclude = "password")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(unique = true, nullable = false)
    public String username;

    @Column(nullable = false)
    public String password;

    @Column(nullable = false)
//    @Enumerated(EnumType.STRING)
    public String role = Role.Name.USER; // ToDo: Needs to be reworked to use enum
}
