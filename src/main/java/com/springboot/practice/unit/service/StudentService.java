package com.springboot.practice.unit.service;

import com.springboot.practice.dto.StudentDTO;

public interface StudentService {

    StudentDTO createStudent(String firstName, String lastName, Integer age);

    StudentDTO getStudent(Integer studentId);

    StudentDTO updateStudent(StudentDTO studentDTO);

    int getNumberOfCoursesAssignedToStudent(Integer studentId);

    void deleteStudent(Integer studentId);
}
