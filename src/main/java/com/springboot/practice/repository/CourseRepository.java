package com.springboot.practice.repository;

import com.springboot.practice.model.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends CrudRepository<Course, Integer> {

    Course findCourseById(Integer id);

    List<Course> findAllCourses();

    List<Course> findAllByTeacher(Integer teacherId);
}
