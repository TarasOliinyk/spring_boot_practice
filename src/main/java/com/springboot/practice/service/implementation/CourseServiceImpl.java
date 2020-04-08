package com.springboot.practice.service.implementation;

import com.springboot.practice.model.Course;
import com.springboot.practice.model.Teacher;
import com.springboot.practice.repository.CourseRepository;
import com.springboot.practice.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course createCourse(String courseName) {
        return courseRepository.save(new Course(courseName));
    }

    @Override
    public Course getCourse(Integer id) {
        return courseRepository.findCourseById(id);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAllCourses();
    }

    @Override
    public Course assignTeachersToCourse(Integer courseId, List<Teacher> teachers) {
        Course course = getCourse(courseId);
        course.setTeachers(teachers);
        return courseRepository.save(course);
    }

    @Override
    public Course unassignTeacherFromCourse(Integer courseId, Teacher teacher) {
        Course course = getCourse(courseId);
        getCourse(courseId).getTeachers().remove(teacher);
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getCoursesWithNumberOfAssignedTeachers(int numberOfTeachers) {
        return getAllCourses().stream().filter(course -> course.getTeachers().size() == numberOfTeachers)
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> getAllCoursesAssignedToTeacher(Integer teacherId) {
        return courseRepository.findAllByTeacher(teacherId);
//        return getAllCourses().stream().filter(course -> course.getTeacher().getId().equals(teacherId))
//                .collect(Collectors.toList());
    }

    @Override
    public void deleteCourse(Integer courseId) {
        courseRepository.deleteById(courseId);
    }
}
