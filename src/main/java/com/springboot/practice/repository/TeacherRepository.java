package com.springboot.practice.repository;

import com.springboot.practice.model.Course;
import com.springboot.practice.model.Teacher;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends CrudRepository<Teacher, Integer>, PagingAndSortingRepository<Teacher, Integer> {

    Teacher findOneById(Integer id);

    List<Teacher> findAll();

    List<Teacher> findAll(Sort sort);
}
