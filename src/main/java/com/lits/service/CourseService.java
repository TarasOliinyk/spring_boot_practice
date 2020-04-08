package com.lits.service;

import com.lits.model.Course;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class CourseService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Adds new course to the table 'course' in the DB.
     *
     * @param course - Specifies the course intended to be added
     */
    @Transactional
    public void addCourse(Course course) {
        entityManager.persist(course);
    }

    /**
     * Retrieves specific course form the table 'course' in the DB.
     *
     * @param courseId - Specifies id of the course intended to be retrieved
     * @return {@code Course}
     */
    public Course getCourse(Integer courseId) {
        return entityManager.find(Course.class, courseId);
    }

    /**
     * Retrieves all courses available in the table 'course' in the DB.
     *
     * @return {@code List<Course>}
     */
    @SuppressWarnings("unchecked")
    public List<Course> getAllCourses() {
        return entityManager.createQuery("SELECT c FROM Course c").getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Course> getAllCoursesWithoutAssignedTeacher() {
        return entityManager.createQuery("SELECT c FROM Course c WHERE c.teacher = null").getResultList();
    }

    /**
     * Updates specific course in the table 'course' in the DB.
     *
     * @param course - Specifies the course intended to be updated (this object should contain new course data)
     * @return {@code Course}
     */
    @Transactional
    public Course updateCourse(Course course) {
        return entityManager.merge(course);
    }

    /**
     * Deletes specific course from the 'course' table in the DB.
     *
     * @param courseId - Specifies id of the course intended to be deleted
     */
    @Transactional
    public void deleteCourse(Integer courseId) {
        entityManager.remove(entityManager.find(Course.class, courseId));
    }
}
