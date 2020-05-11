package com.springboot.practice.repository;

import com.springboot.practice.model.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {

    Optional<Student> findOneById(Integer id);
}
