package com.springboot.practice.repository;

import com.springboot.practice.model.Course;

import java.util.List;

public interface CustomCourseRepository {

    List<Course> findAllByTeachersCount(int numberOfTeachers);
}
