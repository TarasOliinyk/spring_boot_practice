package com.springboot.practice.unit.service;

import com.springboot.practice.dto.CourseDTO;
import com.springboot.practice.dto.StudentDTO;
import com.springboot.practice.dto.TeacherDTO;
import com.springboot.practice.unit.service.criteria.CourseCriteria;

import java.time.LocalDate;
import java.util.List;

public interface CourseService {

    CourseDTO createCourse(String courseName);

    CourseDTO createCourseWithStartAndEndDates(String courseName, LocalDate startDate, LocalDate endDate);

    CourseDTO updateCourse(CourseDTO courseDTO);

    CourseDTO getCourse(Integer id);

    List<CourseDTO> getAllCourses();

    List<CourseDTO> getAllCoursesAssignedToTeacher(TeacherDTO teacherDTO);

    List<CourseDTO> getAllCoursesAssignedToStudent(StudentDTO studentDTO);

    List<CourseDTO> getAllCoursesWithAssignedTeacherAndStudent(TeacherDTO teacherDTO, StudentDTO studentDTO);

    CourseDTO assignTeacherToCourse(CourseDTO courseDTO, TeacherDTO teacherDTO);

    CourseDTO assignStudentToCourse(CourseDTO courseDTO, StudentDTO studentDTO);

    CourseDTO unassignTeacherFromCourse(CourseDTO courseDTO, TeacherDTO teacherDTO);

    CourseDTO unassignStudentFromCourse(CourseDTO courseDTO, StudentDTO studentDTO);

    List<CourseDTO> getCoursesWithNumberOfAssignedTeachers(int numberOfTeachers);

    List<CourseDTO> getFilteredCourses(CourseCriteria criteria);

    List<CourseDTO> getCoursesThatLast(int numberOfDays);

    void deleteCourse(Integer courseId);
}
