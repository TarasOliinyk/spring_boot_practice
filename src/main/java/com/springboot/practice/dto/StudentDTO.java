package com.springboot.practice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = "courses")
@ToString(exclude = "courses")
public class StudentDTO {

    private Integer id;

    private String firstName;

    private String lastName;

    private Integer age;

    @JsonIgnore
    private List<CourseDTO> courses = new ArrayList<>();
}
