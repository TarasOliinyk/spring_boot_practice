package com.springboot.practice.dto;

import com.springboot.practice.model.Course;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TeacherDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private Integer age;
    private List<Course> courses = new ArrayList<>();
}
