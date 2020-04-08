package com.lits.service;

import com.lits.model.Course;
import com.lits.model.Teacher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class TeacherService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Adds new teacher to the table 'teacher' to the DB.
     *
     * @param teacher - Specifies the teacher intended to be added
     */
    @Transactional
    public void addTeacher(Teacher teacher) {
        entityManager.persist(teacher);
    }

    /**
     * Retrieves specific teacher from the table 'teacher' in the DB.
     *
     * @param teacherId - Specifies id of the teacher intended to be retrieved
     * @return {@code Teacher}
     */
    public Teacher getTeacher(Integer teacherId) {
        return entityManager.find(Teacher.class, teacherId);
    }

    /**
     * Retrieves all teachers available in the 'teacher' table in the DB.
     *
     * @return {@code List<Teacher>}
     */
    @SuppressWarnings("unchecked")
    public List<Teacher> getAllTeachers() {
        return entityManager.createQuery("SELECT t FROM Teacher t").getResultList();
    }

    /**
     * Updates specific teacher in the table 'teacher' in the DB.
     *
     * @param teacher - Specifies the teacher intended to be updated (this object should contain new teacher data)
     * @return {@code Teacher}
     */
    @Transactional
    public Teacher updateTeacher(Teacher teacher) { // ToDo: Dirty checking doesn't work for some reason - needs to be investigated
        Teacher originalTeacher = entityManager.find(Teacher.class, teacher.getId());
        String firstNameToUpdate = teacher.getFirstName();
        String lastNameToUpdate = teacher.getLastName();

        if (firstNameToUpdate == null || firstNameToUpdate.equals("")) {
            teacher.setFirstName(originalTeacher.getFirstName());
        }

        if (lastNameToUpdate == null || lastNameToUpdate.equals("")) {
            teacher.setLastName(originalTeacher.getLastName());
        }
        return entityManager.merge(teacher);
    }

    /**
     * Deletes specific teacher from the 'teacher' table in the DB.
     *
     * @param teacherId - Specifies id of the teacher intended to be deleted
     */
    @Transactional
    public void deleteTeacher(Integer teacherId) {
        entityManager.remove(entityManager.find(Teacher.class, teacherId));
    }

    /**
     * Adds specific course to specific teacher.
     *
     * @param teacherId - Specifies id of the teacher the course is intended to be added to
     * @param course    - Specifies the course intended to be added
     */
    @Transactional
    public void addCourseToTeacher(Integer teacherId, Course course) {
        Teacher teacher = getTeacher(teacherId);
        teacher.getCourses().add(course);
        updateTeacher(teacher);
    }

    /**
     * Gets courses assigned to specific teacher.
     *
     * @param teacherId - Specifies id of the teacher, courses of whom are expected to be retrieved
     * @return {@code List<Course>}
     */
    public List<Course> getCoursesAssignedToTeacher(Integer teacherId) {
        return getTeacher(teacherId).getCourses();
    }

    /**
     * Gets number of courses assigned to specific teacher.
     *
     * @param teacherId - Specifies id of the teacher number of courses of whom are expected to be obtained
     * @return {@code int}
     */
    public int getNumberOfCoursesAssignedToTeacher(Integer teacherId) {
        return getCoursesAssignedToTeacher(teacherId).size();
    }


    /**
     * Reassignes course from one teacher to another.
     *
     * @param course            - Specifies the course intended to be reassigned
     * @param originalTeacherId - Specifies id of the teacher the course belonged to
     * @param newTeacherId      - Specifies id of the teacher the course is intended to be reassigned to
     */
    @Transactional
    public void reassignCourse(Course course, Integer originalTeacherId, Integer newTeacherId) { // ToDo: Doesn't work - ask lecture to help w/ this one
        addCourseToTeacher(newTeacherId, course);
        deleteCourseAssignedToTeacher(originalTeacherId, course);
    }

    /**
     * Deletes specific course assigned to specific teacher.
     *
     * @param teacherId - Specifies id of the teacher teh course belongs to
     * @param course    - Specifies id of the course intended to be deleted
     */
    @Transactional
    public void deleteCourseAssignedToTeacher(Integer teacherId, Course course) {
        Teacher teacher = getTeacher(teacherId);
        teacher.getCourses().remove(course);
        updateTeacher(teacher);
    }
}
