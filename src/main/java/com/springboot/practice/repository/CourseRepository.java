package com.springboot.practice.repository;

import com.springboot.practice.model.Course;
import com.springboot.practice.model.Teacher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CourseRepository extends CrudRepository<Course, Integer>, CustomCourseRepository {

    Course findOneById(Integer id);

    List<Course> findAll();

    List<Course> findAllByTeachersContaining(Teacher teacher);

    List<Course> findAllByStartDateGreaterThan(LocalDate date);

    List<Course> findAllByEndDateLessThan(LocalDate date);

    List<Course> findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate startDateComparisonDate, LocalDate endDateComparisonDate);

    @Query("SELECT c FROM Course c WHERE function('TIMESTAMPDIFF', DAY, c.startDate, c.endDate) = ?1")
    List<Course> findAllByDateDiffBetweenStartDateAndEndDateEqualTo(int numberOfDays);
}
