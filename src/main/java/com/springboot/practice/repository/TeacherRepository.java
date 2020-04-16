package com.springboot.practice.repository;

import com.springboot.practice.model.Course;
import com.springboot.practice.model.Teacher;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends CrudRepository<Teacher, Integer> {

    Optional<Teacher> findOneById(Integer id);

    List<Teacher> findAll();

    List<Teacher> findAll(Sort sort);

    List<Teacher> findAllByCoursesContaining(Course course);
}
