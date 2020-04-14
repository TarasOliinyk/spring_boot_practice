package com.springboot.practice.service.implementation;

import com.springboot.practice.model.Course;
import com.springboot.practice.model.Teacher;
import com.springboot.practice.repository.CourseRepository;
import com.springboot.practice.service.CourseService;
import com.springboot.practice.service.criteria.CourseCriteria;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public Course createCourseWithStartAndEndDates(String courseName, LocalDate startDate, LocalDate endDate) {
        return courseRepository.save(new Course(courseName, startDate, endDate));
    }

    @Override
    public Course updateCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course getCourse(Integer id) {
        return courseRepository.findOneById(id);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course assignTeacherToCourse(Course course, Teacher teacher) {
        course.getTeachers().add(teacher);
        return courseRepository.save(course);
    }

    @Override
    public Course unassignTeacherFromCourse(Course course, Teacher teacher) {
        course.getTeachers().remove(teacher);
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getCoursesWithNumberOfAssignedTeachers(int numberOfTeachers) {
        return getAllCourses().stream().filter(course -> course.getTeachers().size() == numberOfTeachers)
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> getFilteredCourses(CourseCriteria criteria) {
        List<Course> filteredCourses;

        switch (criteria) {
            case NOT_STARTED:
                filteredCourses = courseRepository.findAllByStartDateGreaterThan(LocalDate.now());
                break;
            case FINISHED:
                filteredCourses = courseRepository.findAllByEndDateLessThan(LocalDate.now());
                break;
            case ONGOING:
                filteredCourses = courseRepository.findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate.now(), LocalDate.now());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + criteria);
        }
        return filteredCourses;
    }

    @Override
    public List<Course> getCoursesThatLast(int numberOfDays) {
        return courseRepository.findAllByDateDiffBetweenStartDateAndEndDateEqualTo(numberOfDays);
//        return getAllCourses().stream().filter(
//                course -> ChronoUnit.DAYS.between(course.getStartDate(), course.getEndDate()) == numberOfDays)
//                .collect(Collectors.toList());
    }

    @Override
    public List<Course> getAllCoursesAssignedToTeacher(Teacher teacher) {
        return courseRepository.findAllByTeachersContaining(teacher);
    }

    @Override
    public void deleteCourse(Integer courseId) {
        courseRepository.deleteById(courseId);
    }
}
