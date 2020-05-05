package com.springboot.practice.controller;

import com.springboot.practice.dto.StudentDTO;
import com.springboot.practice.unit.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/student")
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO studentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(studentDTO.getFirstName(),
                studentDTO.getLastName(), studentDTO.getAge()));
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(studentService.getStudent(id));
    }

    @PutMapping(value = "/student")
    public ResponseEntity<StudentDTO> updateStudent(@RequestBody StudentDTO studentDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.updateStudent(studentDTO));
    }

    @DeleteMapping("/student/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void deleteTeacher(@PathVariable(name = "id") Integer id) {
        studentService.getStudent(id);
    }
}