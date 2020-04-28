package com.springboot.practice.controller;

import com.springboot.practice.dto.CourseDTO;
import com.springboot.practice.dto.TeacherDTO;
import com.springboot.practice.exceptions.teacher.IllegalTeacherSearchException;
import com.springboot.practice.unit.service.CourseService;
import com.springboot.practice.unit.service.TeacherService;
import com.springboot.practice.unit.service.criteria.TeacherSortingCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TeacherController {
    private final TeacherService teacherService;
    private final CourseService courseService;

    public TeacherController(TeacherService teacherService, CourseService courseService) {
        this.teacherService = teacherService;
        this.courseService = courseService;
    }

    @PostMapping("/teacher")
    public ResponseEntity<TeacherDTO> createTeacher(@RequestBody TeacherDTO teacherDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherService.createTeacher(teacherDTO.getFirstName(),
                teacherDTO.getLastName(), teacherDTO.getAge()));
    }

    @GetMapping("/teacher/{id}")
    public ResponseEntity<TeacherDTO> getTeacher(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(teacherService.getTeacher(id));
    }

    @GetMapping("/teacher/list")
    @ResponseStatus(value = HttpStatus.FOUND)
    public List<TeacherDTO> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/teachers/sorted_by_age/{sortingOrder}")
    @ResponseStatus(HttpStatus.OK)
    public List<TeacherDTO> getAllTeachersSortedByAge(@PathVariable(value = "sortingOrder") String sortingOrder) {
        TeacherSortingCriteria sortingCriteria;

        switch (sortingOrder) {
            case "asc":
                sortingCriteria = TeacherSortingCriteria.ASCENDING_BY_AGE;
                break;
            case "desc":
                sortingCriteria = TeacherSortingCriteria.DESCENDING_BY_AGE;
                break;
            default:
                throw new IllegalTeacherSearchException("Passed order parameter is not supported." +
                        "\nSupported parameters: 'asc' - for ascending sorting and 'desc' - for descending sorting.");
        }
        return teacherService.getAllTeachersSortedBy(sortingCriteria);
    }

    @GetMapping(value = "/teachers/course/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public List<TeacherDTO> getAllTeachersAssignedToCourse(@PathVariable(name = "id") Integer id) {
        CourseDTO course = courseService.getCourse(id);
        return teacherService.getAllTeachersAssignedToCourse(course);
    }

    @DeleteMapping("/teacher/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void deleteTeacher(@PathVariable(name = "id") Integer id) {
        teacherService.deleteTeacher(id);
    }
}
