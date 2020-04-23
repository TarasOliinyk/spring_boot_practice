package com.springboot.practice.controller;

import com.springboot.practice.dto.CourseDTO;
import com.springboot.practice.exceptions.course.IllegalCourseSearchException;
import com.springboot.practice.model.Course;
import com.springboot.practice.model.Teacher;
import com.springboot.practice.service.CourseService;
import com.springboot.practice.service.TeacherService;
import com.springboot.practice.service.criteria.CourseCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CourseController {
    private final CourseService courseService;
    private final TeacherService teacherService;

    public CourseController(CourseService courseService, TeacherService teacherService) {
        this.courseService = courseService;
        this.teacherService = teacherService;
    }

    @PostMapping("/course")
    public ResponseEntity<Course> createCourse(@RequestBody CourseDTO courseDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(courseDTO.getName()));
    }

    @PostMapping("/course_with_dates")
    public ResponseEntity<Course> createCourseWithStartAndEndDates(@RequestBody CourseDTO courseDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourseWithStartAndEndDates(
                courseDTO.getName(), courseDTO.getStartDate(), courseDTO.getEndDate()));
    }

    @GetMapping(value = "/course/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(courseService.getCourse(id));
    }

    @GetMapping(value = "/course/list")
    @ResponseStatus(value = HttpStatus.FOUND)
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/courses/teacher/{teacherId}")
    @ResponseStatus(HttpStatus.FOUND)
    public List<Course> getAllCoursesAssignedToTeacher(@PathVariable(name = "teacherId") Integer teacherId) {
        Teacher teacher = teacherService.getTeacher(teacherId);
        return courseService.getAllCoursesAssignedToTeacher(teacher);
    }

    @PutMapping(value = "/course/{courseId}/add_teacher/{teacherId}")
    public ResponseEntity<Course> assignTeacher(@PathVariable(value = "courseId") Integer courseId,
                                                @PathVariable(value = "teacherId") Integer teacherId) {
        Course course = courseService.getCourse(courseId);
        Teacher teacher = teacherService.getTeacher(teacherId);
        return ResponseEntity.status(HttpStatus.OK).body(courseService.assignTeacherToCourse(course, teacher));
    }

    @PutMapping("/course/{courseId}/remove_teacher/{teacherId}")
    public ResponseEntity<Course> unassignTeacher(@PathVariable(value = "courseId") Integer courseId,
                                                  @PathVariable(value = "teacherId") Integer teacherId) {
        Course course = courseService.getCourse(courseId);
        Teacher teacher = course.getTeachers().stream().filter(t -> t.getId().equals(teacherId)).findFirst().orElseThrow(
                () -> new RuntimeException(String.format("The course with id '%s' doesn't have assigned teacher with id '%s'",
                        courseId, teacherId)));
        return ResponseEntity.status(HttpStatus.OK).body(courseService.unassignTeacherFromCourse(course, teacher));
    }

    @GetMapping("/course/teachers/{numberOfTeachers}")
    @ResponseStatus(HttpStatus.FOUND)
    public List<Course> getCoursesWithNumberOfAssignedTeachers(@PathVariable(value = "numberOfTeachers") int numberOfTeachers) {
        return courseService.getCoursesWithNumberOfAssignedTeachers(numberOfTeachers);
    }

    @GetMapping("/courses/filter/{sortingCriteria}")
    @ResponseStatus(HttpStatus.OK)
    public List<Course> getFilteredCourses(@PathVariable(value = "sortingCriteria") String sortingCriteria) {
        CourseCriteria courseCriteria;

        switch (sortingCriteria) {
            case "not_started":
                courseCriteria = CourseCriteria.NOT_STARTED;
                break;
            case "finished":
                courseCriteria = CourseCriteria.FINISHED;
                break;
            case "ongoing":
                courseCriteria = CourseCriteria.ONGOING;
                break;
            default:
                throw new IllegalCourseSearchException("Passed filtering criteria is not supported." +
                        "\nSupported criteria: " +
                        "\n > 'not_started' - for getting not started courses;" +
                        "\n > 'finished' - for getting finished courses;" +
                        "\n > 'ongoing' - for getting ongoing courses;");

        }
        return courseService.getFilteredCourses(courseCriteria);
    }

    @GetMapping("/courses/duration/{numberOfDays}")
    @ResponseStatus(HttpStatus.FOUND)
    public List<Course> getCoursesThatLastSpecificNumberOfDays(@PathVariable(value = "numberOfDays") int numberOfDays) {
        return courseService.getCoursesThatLast(numberOfDays);
    }

    @DeleteMapping(value = "/course/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void deleteCourse(@PathVariable(value = "id") Integer id) {
        courseService.deleteCourse(id);
    }
}
