package com.springboot.practice.service;

import com.springboot.practice.model.Teacher;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface TeacherService {

    Teacher createTeacher(String firstName, String lastName, Integer age);

    Teacher getTeacher(Integer teacherId);

    List<Teacher> getAllTeachers();

    List<Teacher> getAllTeachersSortedBy(Sort sort);

    Teacher getTeacherAssignedToCourse(Integer courseId);

    void deleteTeacher(Integer teacherId);
}
