package com.springboot.practice.service;

import com.springboot.practice.model.Course;
import com.springboot.practice.model.Teacher;

import java.time.LocalDate;
import java.util.List;

public interface CourseService {

    Course createCourse(String courseName);

    Course createCourseWithStartAndEndDates(String courseName, LocalDate startDate, LocalDate endDate);

    Course updateCourse(Course course);

    Course getCourse(Integer id);

    List<Course> getAllCourses();

    List<Course> getAllCoursesAssignedToTeacher(Teacher teacher);

    Course assignTeacherToCourse(Course course, Teacher teacher);

    Course unassignTeacherFromCourse(Course course, Teacher teacher);

    List<Course> getCoursesWithNumberOfAssignedTeachers(int numberOfTeachers);

    public List<Course> getNotStartedCourses();

    public List<Course> getFinishedCourses();

    public List<Course> getOngoingCourses();

    public List<Course> getCoursesThatLast(int numberOfDays);

    void deleteCourse(Integer courseId);
}
