package com.springboot.practice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString(exclude = "courses")
@EqualsAndHashCode(exclude = "courses")
@NoArgsConstructor
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @NotEmpty(message = "Teacher cannot be registered without first name")
    private String firstName;

    @Column
    @NotEmpty(message = "Teacher cannot be registered without last name")
    private String lastName;

    @Column
    @Positive(message = "Teacher's age cannot be a negative number")
    @Min(value = 16, message = "Teacher has to be at least 16 years old")
    public Integer age;

    @JsonIgnore
    @ManyToMany(mappedBy = "teachers")
    private List<Course> courses = new ArrayList<>();

    public Teacher(String firstName, String lastName, Integer age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
}
