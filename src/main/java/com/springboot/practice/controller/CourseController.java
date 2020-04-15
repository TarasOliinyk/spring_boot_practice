package com.springboot.practice.controller;

import com.springboot.practice.dto.CourseDTO;
import com.springboot.practice.model.Course;
import com.springboot.practice.model.Teacher;
import com.springboot.practice.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Controller
public class CourseController { // ToDo: Not needed for console app implementation
//
//    private final CourseService courseService;
//
//    public CourseController(CourseService courseService) {
//        this.courseService = courseService;
//    }
//
//    @PostMapping("/course")
//    public ResponseEntity<Course> createCourse(@RequestBody CourseDTO courseDTO) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(courseDTO.getName()));
//    }
//
//    @GetMapping(value = "/course/{id}")
//    public ResponseEntity<Course> getCourse(@PathVariable(name = "id") Integer id) {
//        return ResponseEntity.status(HttpStatus.FOUND).body(courseService.getCourse(id));
//    }
//
//    @GetMapping(value = "/course/list")
//    @ResponseStatus(value = HttpStatus.FOUND)
//    public List<Course> getAllCourses() {
//        return courseService.getAllCourses();
//    }
//
//    @PutMapping(value = "/course/{id}")
//    public ResponseEntity<Course> assignTeachers(@PathVariable(value = "id") Integer id, @RequestBody CourseDTO courseDTO) {
//        return ResponseEntity.status(HttpStatus.OK).body(courseService.assignTeachersToCourse(id, courseDTO.getTeachers()));
//    }
//
//    @PutMapping("/course/{courseId}/teacher/{teacherId}")
//    public ResponseEntity<Course> unassignTeacher(@PathVariable(value = "courseId") Integer courseId,
//                                                  @PathVariable(value = "teacherId") Integer teacherId,
//                                                  @RequestBody CourseDTO courseDTO) {
//        Teacher teacher = courseDTO.getTeachers().stream().filter(t -> t.getId().equals(teacherId)).findFirst().orElseThrow(
//                () -> new RuntimeException(String.format("The course with id '%s' doesn't have assigned teacher with id '%s'",
//                        courseId, teacherId)));
//        return ResponseEntity.status(HttpStatus.OK).body(courseService.unassignTeacherFromCourse(courseId, teacher));
//    }
//
//    @GetMapping("/course/teachers/{numberOfTeachers}")
//    @ResponseStatus(HttpStatus.FOUND)
//    public List<Course> getCoursesWithNumberOfAssignedTeachers(@PathVariable(value = "numberOfTeachers") int numberOfTeachers) {
//        return courseService.getCoursesWithNumberOfAssignedTeachers(numberOfTeachers);
//    }
//
//    @GetMapping("/course/teacher/{id}")
//    public List<Course> getAllCoursesAssignedToTeacher(@PathVariable(name = "id") Integer id) {
//        return courseService.getAllCoursesAssignedToTeacher(id);
//    }
//
//    @DeleteMapping(value = "/course/{id}")
//    @ResponseStatus(value = HttpStatus.ACCEPTED)
//    public void deleteCourse(@PathVariable(value = "id") Integer id) {
//        courseService.deleteCourse(id);
//    }
}
