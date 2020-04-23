package com.springboot.practice.service;

import com.springboot.practice.model.Course;
import com.springboot.practice.model.Teacher;
import com.springboot.practice.service.criteria.TeacherSortingCriteria;

import java.util.List;

public interface TeacherService {

    Teacher createTeacher(String firstName, String lastName, Integer age);

    Teacher getTeacher(Integer teacherId);

    List<Teacher> getAllTeachers();

    List<Teacher> getAllTeachersSortedBy(TeacherSortingCriteria sortingParameter);

    List<Teacher> getAllTeachersAssignedToCourse(Course course);

    void deleteTeacher(Integer teacherId);
}
