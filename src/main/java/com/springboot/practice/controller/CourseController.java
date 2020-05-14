package com.springboot.practice.controller;

import com.springboot.practice.annotation.IsAdmin;
import com.springboot.practice.data.Role;
import com.springboot.practice.dto.CourseDTO;
import com.springboot.practice.exceptions.course.IllegalCourseSearchException;
import com.springboot.practice.unit.service.CourseService;
import com.springboot.practice.unit.service.criteria.CourseCriteria;
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

    @IsAdmin
    @PostMapping(path = "/course")
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(courseDTO.getName()));
    }

    @IsAdmin
    @PostMapping(path = "/course_with_dates")
    public ResponseEntity<CourseDTO> createCourseWithStartAndEndDates(@RequestBody CourseDTO courseDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourseWithStartAndEndDates(
                courseDTO.getName(), courseDTO.getStartDate(), courseDTO.getEndDate()));
    }

    @RolesAllowed({Role.Name.USER, Role.Name.STUDENT, Role.Name.TEACHER})
    @GetMapping(path = "/course/{id}")
    public ResponseEntity<CourseDTO> getCourse(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(courseService.getCourse(id));
    }

    @RolesAllowed({Role.Name.USER, Role.Name.STUDENT, Role.Name.TEACHER})
    @GetMapping(path = "/course/list")
    @ResponseStatus(value = HttpStatus.FOUND)
    public List<CourseDTO> getAllCourses() {
        return courseService.getAllCourses();
    }

    @IsAdmin
    @GetMapping(path = "/courses/teacher/{teacherId}")
    @ResponseStatus(HttpStatus.FOUND)
    public List<CourseDTO> getAllCoursesAssignedToTeacher(@PathVariable(name = "teacherId") Integer teacherId) {
        return courseService.getAllCoursesAssignedToTeacher(teacherId);
    }

    @IsAdmin
    @GetMapping(path = "/courses/student/{studentId}")
    @ResponseStatus(HttpStatus.FOUND)
    public List<CourseDTO> getAllCoursesAssignedToStudent(@PathVariable(name = "studentId") Integer studentId) {
        return courseService.getAllCoursesAssignedToStudent(studentId);
    }

    @IsAdmin
    @GetMapping(path = "/courses/teacher/{teacherId}/student/{studentId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CourseDTO> getAllCoursesWithAssignedTeacherAndStudent(@PathVariable(name = "teacherId") Integer teacherId,
                                                                      @PathVariable(name = "studentId") Integer studentId) {
        return courseService.getAllCoursesWithAssignedTeacherAndStudent(teacherId, studentId);
    }

    @IsAdmin
    @PutMapping(path = "/course/{courseId}/add_teacher/{teacherId}")
    public ResponseEntity<CourseDTO> assignTeacher(@PathVariable(value = "courseId") Integer courseId,
                                                   @PathVariable(value = "teacherId") Integer teacherId) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.assignTeacherToCourse(courseId, teacherId));
    }

    @IsAdmin
    @PutMapping(path = "/course/{courseId}/add_student/{studentId}")
    public ResponseEntity<CourseDTO> assignStudent(@PathVariable(value = "courseId") Integer courseId,
                                                   @PathVariable(value = "studentId") Integer studentId) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.assignStudentToCourse(courseId, studentId));
    }

    @IsAdmin
    @PutMapping(path = "/course/{courseId}/remove_teacher/{teacherId}")
    public ResponseEntity<CourseDTO> unassignTeacher(@PathVariable(value = "courseId") Integer courseId,
                                                     @PathVariable(value = "teacherId") Integer teacherId) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.unassignTeacherFromCourse(courseId, teacherId));
    }

    @IsAdmin
    @PutMapping(path = "/course/{courseId}/remove_student/{studentId}")
    public ResponseEntity<CourseDTO> unassignStudent(@PathVariable(value = "courseId") Integer courseId,
                                                     @PathVariable(value = "studentId") Integer studentId) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.unassignStudentFromCourse(courseId, studentId));
    }

    @IsAdmin
    @GetMapping(path = "/course/teachers/{numberOfTeachers}")
    @ResponseStatus(HttpStatus.FOUND)
    public List<CourseDTO> getCoursesWithNumberOfAssignedTeachers(@PathVariable(value = "numberOfTeachers") int numberOfTeachers) {
        return courseService.getCoursesWithNumberOfAssignedTeachers(numberOfTeachers);
    }

    @RolesAllowed({Role.Name.USER, Role.Name.STUDENT, Role.Name.TEACHER})
    @GetMapping(path = "/courses/filter/{sortingCriteria}")
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

    @RolesAllowed({Role.Name.USER, Role.Name.STUDENT, Role.Name.TEACHER})
    @GetMapping(path = "/courses/duration/{numberOfDays}")
    @ResponseStatus(HttpStatus.FOUND)
    public List<CourseDTO> getCoursesThatLastSpecificNumberOfDays(@PathVariable(value = "numberOfDays") int numberOfDays) {
        return courseService.getCoursesThatLast(numberOfDays);
    }

    @IsAdmin
    @DeleteMapping(path = "/course/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteCourse(@PathVariable(value = "id") Integer id) {
        courseService.deleteCourse(id);
    }
}
