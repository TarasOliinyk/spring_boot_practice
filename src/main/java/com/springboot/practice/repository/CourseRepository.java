package com.springboot.practice.repository;

import com.springboot.practice.model.Course;
import com.springboot.practice.model.Student;
import com.springboot.practice.model.Teacher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends CrudRepository<Course, Integer>, CustomCourseRepository {

    Optional<Course> findOneById(Integer id);

    List<Course> findAll();

    List<Course> findAllByTeachersContaining(Teacher teacher);

    List<Course> findAllByStudentsContaining(Student student);

    List<Course> findAllByTeachersContainingAndStudentsContaining(Teacher teacher, Student student);

    List<Course> findAllByStartDateGreaterThan(LocalDate date);

    List<Course> findAllByEndDateLessThan(LocalDate date);

    List<Course> findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate startDateComparisonDate, LocalDate endDateComparisonDate);

    @Query("SELECT c FROM Course c WHERE function('TIMESTAMPDIFF', DAY, c.startDate, c.endDate) = ?1")
    List<Course> findAllByDateDiffBetweenStartDateAndEndDateEqualTo(int numberOfDays);
}
