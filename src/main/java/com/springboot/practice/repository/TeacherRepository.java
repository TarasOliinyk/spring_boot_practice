package com.springboot.practice.repository;

import com.springboot.practice.model.Teacher;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends CrudRepository<Teacher, Integer> {

    Teacher findTeacherById(Integer id);

    List<Teacher> findAllTeachers();

    List<Teacher> findAllTeachersSortedBy(Sort sort);
}
