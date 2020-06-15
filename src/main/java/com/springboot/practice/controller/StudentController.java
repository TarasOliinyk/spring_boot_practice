package com.springboot.practice.controller;

import com.springboot.practice.annotation.permission.student.*;
import com.springboot.practice.data.UserRole;
import com.springboot.practice.dto.StudentDTO;
import com.springboot.practice.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping(path = "/student")
    @HasStudentCreatePermission
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO studentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(studentDTO));
    }

    @GetMapping(path = "/student/{id}")
    @HasStudentReadPermission
    public ResponseEntity<StudentDTO> getStudent(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(studentService.getStudent(id));
    }

    @PutMapping(path = "/student")
    @HasStudentUpdatePermission
    public ResponseEntity<StudentDTO> updateStudent(@RequestBody StudentDTO studentDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.updateStudent(studentDTO));
    }

    @GetMapping(path = "/student/{studentId}/courses_count")
    @RolesAllowed({UserRole.Name.STUDENT, UserRole.Name.ADMIN})
    public ResponseEntity<Integer> getNumberOfCoursesAssignedToStudent(@PathVariable(name = "studentId") Integer studentId) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getNumberOfCoursesAssignedToStudent(studentId));
    }

    @DeleteMapping(path = "/student/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @HasStudentDeletePermission
    public void deleteStudent(@PathVariable(name = "id") Integer id) {
        studentService.deleteStudent(id);
    }
}
