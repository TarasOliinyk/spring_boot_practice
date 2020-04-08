package com.springboot.practice.controller;

import com.springboot.practice.dto.TeacherDTO;
import com.springboot.practice.model.Teacher;
import com.springboot.practice.service.TeacherService;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    // ToDo: All of the methods need to be tested.

    @PostMapping("/teacher")
    public ResponseEntity<Teacher> createTeacher(@RequestBody TeacherDTO teacherDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherService.createTeacher(teacherDTO.getFirstName(),
                teacherDTO.getLastName(), teacherDTO.getAge()));
    }

    @GetMapping("/teacher/{id}")
    public ResponseEntity<Teacher> getTeacher(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(teacherService.getTeacher(id));
    }

    @GetMapping("/teacher/list")
    @ResponseStatus(value = HttpStatus.FOUND)
    public List<Teacher> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/teacher/sort/{sortingOrder}")
    @ResponseStatus(HttpStatus.OK)
    public List<Teacher> getAllTeachersSortedByAge(@PathVariable(value = "sortingOrder") String sortingOrder) {
        Sort order;

        switch (sortingOrder) {
            case "asc":
                order = Sort.by(Sort.Order.asc("age"));
                break;
            case "desc":
                order = Sort.by(Sort.Order.desc("age"));
                break;
            default:
                throw new IllegalArgumentException("Passed order parameter is not supported.\nSupported parameters: " +
                        "'asc' - for ascending sorting and 'desc' - for descending sorting.");
        }
        return teacherService.getAllTeachersSortedBy(order);
    }

    @GetMapping(value = "/teacher/course/{id}")
    public ResponseEntity<Teacher> getTeacherAssignedToCourse(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(teacherService.getTeacherAssignedToCourse(id));
    }

    @DeleteMapping("/teacher/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void deleteTeacher(@PathVariable(name = "id") Integer id) {
        teacherService.deleteTeacher(id);
    }
}
