package com.springboot.practice.controller;

import com.springboot.practice.dto.StudentDTO;
import com.springboot.practice.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

//    @IsAdmin // ToDo: Need to set corresponding permission
    @PostMapping(path = "/student")
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO studentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(studentDTO));
    }

//    @RolesAllowed({Role.Name.STUDENT, Role.Name.TEACHER, Role.Name.ADMIN}) // ToDo: Need to set corresponding permission
    @GetMapping(path = "/student/{id}")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(studentService.getStudent(id));
    }

//    @IsAdmin // ToDo: Need to set corresponding permission
    @PutMapping(path = "/student")
    public ResponseEntity<StudentDTO> updateStudent(@RequestBody StudentDTO studentDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.updateStudent(studentDTO));
    }

//    @RolesAllowed({Role.Name.STUDENT, Role.Name.TEACHER, Role.Name.ADMIN}) // ToDo: Need to set corresponding permission
    @GetMapping(path = "/student/{studentId}/courses_count")
    public ResponseEntity<Integer> getNumberOfCoursesAssignedToStudent(@PathVariable(name = "studentId") Integer studentId) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getNumberOfCoursesAssignedToStudent(studentId));
    }

//    @IsAdmin // ToDo: Need to set corresponding permission
    @DeleteMapping(path = "/student/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteStudent(@PathVariable(name = "id") Integer id) {
        studentService.deleteStudent(id);
    }
}
