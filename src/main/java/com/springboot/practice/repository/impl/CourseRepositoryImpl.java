package com.springboot.practice.repository.impl;

import com.springboot.practice.model.Course;
import com.springboot.practice.repository.CustomCourseRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class CourseRepositoryImpl implements CustomCourseRepository {

    private final EntityManager entityManager;

    public CourseRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Course> findAllByTeachersCount(int numberOfTeachers) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Course> cq = cb.createQuery(Course.class);
        Root<Course> course = cq.from(Course.class);
        Predicate predicate = cb.equal(cb.size(course.get("teachers")), numberOfTeachers);
        cq.where(predicate);
        return entityManager.createQuery(cq).getResultList();
    }
}
