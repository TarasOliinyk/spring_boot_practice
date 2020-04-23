package com.springboot.practice.service;

import com.springboot.practice.dto.CourseDTO;
import com.springboot.practice.dto.TeacherDTO;
import com.springboot.practice.service.criteria.CourseCriteria;

import java.time.LocalDate;
import java.util.List;

public interface CourseService {

    CourseDTO createCourse(String courseName);

    CourseDTO createCourseWithStartAndEndDates(String courseName, LocalDate startDate, LocalDate endDate);

    CourseDTO updateCourse(CourseDTO courseDTO);

    CourseDTO getCourse(Integer id);

    List<CourseDTO> getAllCourses();

    List<CourseDTO> getAllCoursesAssignedToTeacher(TeacherDTO teacherDTO);

    CourseDTO assignTeacherToCourse(CourseDTO courseDTO, TeacherDTO teacherDTO);

    CourseDTO unassignTeacherFromCourse(CourseDTO courseDTO, TeacherDTO teacherDTO);

    List<CourseDTO> getCoursesWithNumberOfAssignedTeachers(int numberOfTeachers);

    List<CourseDTO> getFilteredCourses(CourseCriteria criteria);

    List<CourseDTO> getCoursesThatLast(int numberOfDays);

    void deleteCourse(Integer courseId);
}
