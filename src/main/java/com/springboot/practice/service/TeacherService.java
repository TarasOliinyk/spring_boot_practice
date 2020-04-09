package com.springboot.practice.service;

import com.springboot.practice.model.Course;
import com.springboot.practice.model.Teacher;
import com.springboot.practice.service.criteria.TeacherSortingParameter;

import java.util.List;

public interface TeacherService {

    Teacher createTeacher(String firstName, String lastName, Integer age);

    Teacher getTeacher(Integer teacherId);

    List<Teacher> getAllTeachers();

    List<Teacher> getAllTeachersSortedBy(TeacherSortingParameter sortingParameter);

    Teacher getTeacherAssignedToCourse(Integer courseId);

    List<Course> getAllCoursesAssignedToTeacher(Teacher teacher);

    void deleteTeacher(Integer teacherId);
}
