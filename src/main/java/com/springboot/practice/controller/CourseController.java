package com.springboot.practice.controller;

import com.springboot.practice.dto.CourseDTO;
import com.springboot.practice.dto.TeacherDTO;
import com.springboot.practice.exceptions.course.IllegalCourseSearchException;
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
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(courseDTO.getName()));
    }

    @PostMapping("/course_with_dates")
    public ResponseEntity<CourseDTO> createCourseWithStartAndEndDates(@RequestBody CourseDTO courseDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourseWithStartAndEndDates(
                courseDTO.getName(), courseDTO.getStartDate(), courseDTO.getEndDate()));
    }

    @GetMapping(value = "/course/{id}")
    public ResponseEntity<CourseDTO> getCourse(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(courseService.getCourse(id));
    }

    @GetMapping(value = "/course/list")
    @ResponseStatus(value = HttpStatus.FOUND)
    public List<CourseDTO> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/courses/teacher/{teacherId}")
    @ResponseStatus(HttpStatus.FOUND)
    public List<CourseDTO> getAllCoursesAssignedToTeacher(@PathVariable(name = "teacherId") Integer teacherId) {
        TeacherDTO teacherDTO = teacherService.getTeacher(teacherId);
        return courseService.getAllCoursesAssignedToTeacher(teacherDTO);
    }

    @PutMapping(value = "/course/{courseId}/add_teacher/{teacherId}")
    public ResponseEntity<CourseDTO> assignTeacher(@PathVariable(value = "courseId") Integer courseId,
                                                @PathVariable(value = "teacherId") Integer teacherId) {
        CourseDTO courseDTO = courseService.getCourse(courseId);
        TeacherDTO teacherDTO = teacherService.getTeacher(teacherId);
        return ResponseEntity.status(HttpStatus.OK).body(courseService.assignTeacherToCourse(courseDTO, teacherDTO));
    }

    @PutMapping("/course/{courseId}/remove_teacher/{teacherId}")
    public ResponseEntity<CourseDTO> unassignTeacher(@PathVariable(value = "courseId") Integer courseId,
                                                  @PathVariable(value = "teacherId") Integer teacherId) {
        CourseDTO courseDTO = courseService.getCourse(courseId);
        TeacherDTO teacherDTO = teacherService.getTeacher(teacherId);
        return ResponseEntity.status(HttpStatus.OK).body(courseService.unassignTeacherFromCourse(courseDTO, teacherDTO));
    }

    @GetMapping("/course/teachers/{numberOfTeachers}")
    @ResponseStatus(HttpStatus.FOUND)
    public List<CourseDTO> getCoursesWithNumberOfAssignedTeachers(@PathVariable(value = "numberOfTeachers") int numberOfTeachers) {
        return courseService.getCoursesWithNumberOfAssignedTeachers(numberOfTeachers);
    }

    @GetMapping("/courses/filter/{sortingCriteria}")
    @ResponseStatus(HttpStatus.OK)
    public List<CourseDTO> getFilteredCourses(@PathVariable(value = "sortingCriteria") String sortingCriteria) {
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
    public List<CourseDTO> getCoursesThatLastSpecificNumberOfDays(@PathVariable(value = "numberOfDays") int numberOfDays) {
        return courseService.getCoursesThatLast(numberOfDays);
    }

    @DeleteMapping(value = "/course/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void deleteCourse(@PathVariable(value = "id") Integer id) {
        courseService.deleteCourse(id);
    }
}
