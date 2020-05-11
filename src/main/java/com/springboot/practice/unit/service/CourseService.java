package com.springboot.practice.unit.service;

import com.springboot.practice.dto.CourseDTO;
import com.springboot.practice.unit.service.criteria.CourseCriteria;

import java.time.LocalDate;
import java.util.List;

public interface CourseService {

    CourseDTO createCourse(String courseName);

    CourseDTO createCourseWithStartAndEndDates(String courseName, LocalDate startDate, LocalDate endDate);

    CourseDTO updateCourse(CourseDTO courseDTO);

    CourseDTO getCourse(Integer id);

    List<CourseDTO> getAllCourses();

    List<CourseDTO> getAllCoursesAssignedToTeacher(Integer teacherId);

    List<CourseDTO> getAllCoursesAssignedToStudent(Integer studentId);

    List<CourseDTO> getAllCoursesWithAssignedTeacherAndStudent(Integer teacherId, Integer studentId);

    CourseDTO assignTeacherToCourse(Integer courseId, Integer teacherId);

    CourseDTO assignStudentToCourse(Integer courseId, Integer studentId);

    CourseDTO unassignTeacherFromCourse(Integer courseId, Integer teacherId);

    CourseDTO unassignStudentFromCourse(Integer courseId, Integer studentId);

    List<CourseDTO> getCoursesWithNumberOfAssignedTeachers(int numberOfTeachers);

    List<CourseDTO> getFilteredCourses(CourseCriteria criteria);

    List<CourseDTO> getCoursesThatLast(int numberOfDays);

    void deleteCourse(Integer courseId);
}
