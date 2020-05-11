package com.springboot.practice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = "courses")
@ToString(exclude = "courses")
public class TeacherDTO {

    private Integer id;

    @NotBlank(message = "Teacher cannot be registered without first name")
    private String firstName;

    @NotBlank(message = "Teacher cannot be registered without last name")
    private String lastName;

    @Positive(message = "Teacher's age cannot be a negative number")
    @Min(value = 16, message = "Teacher has to be at least 16 years old")
    private Integer age;

    @Pattern(regexp="(^$|[0-9]{10})")
    public String phoneNumber;

    @JsonIgnore
    private List<CourseDTO> courses = new ArrayList<>();
}
