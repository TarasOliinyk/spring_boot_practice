package com.springboot.practice.service;

import com.springboot.practice.model.Course;
import com.springboot.practice.model.Teacher;

import java.util.List;

public interface CourseService {

    Course createCourse(String courseName);

    Course getCourse(Integer id);

    List<Course> getAllCourses();

    List<Course> getAllCoursesAssignedToTeacher(Integer teacherId);

    Course assignTeachersToCourse(Integer courseId, List<Teacher> teachers);

    Course unassignTeacherFromCourse(Integer courseId, Teacher teacher);

    List<Course> getCoursesWithNumberOfAssignedTeachers(int numberOfTeachers);

    void deleteCourse(Integer courseId);
}
