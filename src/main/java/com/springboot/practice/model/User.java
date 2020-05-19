package com.springboot.practice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userId")
    @Fetch(FetchMode.SELECT)
    public List<Role> roles = new ArrayList<>();
}
