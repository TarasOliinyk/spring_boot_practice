package com.springboot.practice.dto;

import com.springboot.practice.model.Teacher;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CourseDTO {
    private Integer id;
    private String name;
    private List<Teacher> teachers;
    private LocalDate startDate;
    private LocalDate endDate;
}
