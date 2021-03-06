package com.springboot.practice.controller;

import com.springboot.practice.annotation.permission.course.*;
import com.springboot.practice.annotation.role.IsAdmin;
import com.springboot.practice.data.UserRole;
import com.springboot.practice.dto.CourseDTO;
import com.springboot.practice.exceptions.course.IllegalCourseSearchException;
import com.springboot.practice.service.CourseService;
import com.springboot.practice.service.criteria.CourseCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping(path = "/course")
    @HasCourseCreatePermission
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(courseDTO.getName()));
    }

    @PostMapping(path = "/course_with_dates")
    @HasCourseCreatePermission
    public ResponseEntity<CourseDTO> createCourseWithStartAndEndDates(@RequestBody CourseDTO courseDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourseWithStartAndEndDates(
                courseDTO.getName(), courseDTO.getStartDate(), courseDTO.getEndDate()));
    }

    @GetMapping(path = "/course/{id}")
    @HasCourseReadPermission
    public ResponseEntity<CourseDTO> getCourse(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(courseService.getCourse(id));
    }

    @GetMapping(path = "/course/list")
    @ResponseStatus(value = HttpStatus.FOUND)
    @IsAdmin
    public List<CourseDTO> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping(path = "/courses/teacher/{teacherId}")
    @ResponseStatus(HttpStatus.FOUND)
    @RolesAllowed({UserRole.Name.TEACHER, UserRole.Name.ADMIN})
    public List<CourseDTO> getAllCoursesAssignedToTeacher(@PathVariable(name = "teacherId") Integer teacherId) {
        return courseService.getAllCoursesAssignedToTeacher(teacherId);
    }

    @GetMapping(path = "/courses/student/{studentId}")
    @ResponseStatus(HttpStatus.FOUND)
    @RolesAllowed({UserRole.Name.STUDENT, UserRole.Name.ADMIN})
    public List<CourseDTO> getAllCoursesAssignedToStudent(@PathVariable(name = "studentId") Integer studentId) {
        return courseService.getAllCoursesAssignedToStudent(studentId);
    }

    @GetMapping(path = "/courses/teacher/{teacherId}/student/{studentId}")
    @ResponseStatus(HttpStatus.OK)
    @IsAdmin
    public List<CourseDTO> getAllCoursesWithAssignedTeacherAndStudent(@PathVariable(name = "teacherId") Integer teacherId,
                                                                      @PathVariable(name = "studentId") Integer studentId) {
        return courseService.getAllCoursesWithAssignedTeacherAndStudent(teacherId, studentId);
    }

    @PutMapping(path = "/course/{courseId}/add_teacher/{teacherId}")
    @HasCourseUpdatePermission
    public ResponseEntity<CourseDTO> assignTeacher(@PathVariable(value = "courseId") Integer courseId,
                                                   @PathVariable(value = "teacherId") Integer teacherId) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.assignTeacherToCourse(courseId, teacherId));
    }

    @PutMapping(path = "/course/{courseId}/add_student/{studentId}")
    @HasCourseUpdatePermission
    public ResponseEntity<CourseDTO> assignStudent(@PathVariable(value = "courseId") Integer courseId,
                                                   @PathVariable(value = "studentId") Integer studentId) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.assignStudentToCourse(courseId, studentId));
    }

    @PutMapping(path = "/course/{courseId}/remove_teacher/{teacherId}")
    @HasCourseUpdatePermission
    public ResponseEntity<CourseDTO> unassignTeacher(@PathVariable(value = "courseId") Integer courseId,
                                                     @PathVariable(value = "teacherId") Integer teacherId) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.unassignTeacherFromCourse(courseId, teacherId));
    }

    @PutMapping(path = "/course/{courseId}/remove_student/{studentId}")
    @HasCourseUpdatePermission
    public ResponseEntity<CourseDTO> unassignStudent(@PathVariable(value = "courseId") Integer courseId,
                                                     @PathVariable(value = "studentId") Integer studentId) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.unassignStudentFromCourse(courseId, studentId));
    }

    @GetMapping(path = "/course/teachers/{numberOfTeachers}")
    @ResponseStatus(HttpStatus.FOUND)
    @IsAdmin
    public List<CourseDTO> getCoursesWithNumberOfAssignedTeachers(@PathVariable(value = "numberOfTeachers") int numberOfTeachers) {
        return courseService.getCoursesWithNumberOfAssignedTeachers(numberOfTeachers);
    }

    @GetMapping(path = "/courses/filter/{sortingCriteria}")
    @ResponseStatus(HttpStatus.OK)
    @HasCourseReadPermission
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

    @GetMapping(path = "/courses/duration/{numberOfDays}")
    @ResponseStatus(HttpStatus.FOUND)
    @RolesAllowed({UserRole.Name.STUDENT, UserRole.Name.ADMIN})
    public List<CourseDTO> getCoursesThatLastSpecificNumberOfDays(@PathVariable(value = "numberOfDays") int numberOfDays) {
        return courseService.getCoursesThatLast(numberOfDays);
    }

    @DeleteMapping(path = "/course/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @HasCourseDeletePermission
    public void deleteCourse(@PathVariable(value = "id") Integer id) {
        courseService.deleteCourse(id);
    }
}
