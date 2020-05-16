package com.springboot.practice.controller;

import com.springboot.practice.annotation.IsAdmin;
import com.springboot.practice.data.Role;
import com.springboot.practice.dto.TeacherDTO;
import com.springboot.practice.exceptions.teacher.IllegalTeacherSearchException;
import com.springboot.practice.unit.service.TeacherService;
import com.springboot.practice.unit.service.criteria.TeacherSortingCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TeacherController {
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @IsAdmin
    @PostMapping(path = "/teacher")
    public ResponseEntity<TeacherDTO> createTeacher(@RequestBody TeacherDTO teacherDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherService.createTeacher(teacherDTO));
    }

    @RolesAllowed({Role.Name.STUDENT, Role.Name.TEACHER, Role.Name.ADMIN})
    @GetMapping(path = "/teacher/{id}")
    public ResponseEntity<TeacherDTO> getTeacher(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(teacherService.getTeacher(id));
    }

    @IsAdmin
    @GetMapping(path = "/teacher/phone_number/{phoneNumber}")
    public ResponseEntity<TeacherDTO> getTeacherByPhoneNumber(@PathVariable(name = "phoneNumber") String phoneNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(teacherService.getTeacherByPhoneNumber(phoneNumber));
    }

    @RolesAllowed({Role.Name.USER, Role.Name.STUDENT, Role.Name.TEACHER, Role.Name.ADMIN})
    @GetMapping(path = "/teacher/list")
    @ResponseStatus(HttpStatus.FOUND)
    public List<TeacherDTO> getAllTeachers() {
        Role userRole = Role.valueOf(SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .iterator().next().getAuthority());

        List<TeacherDTO> teachers = teacherService.getAllTeachers();

        if (userRole.equals(Role.ROLE_USER)) {
            return teachers.stream().map(teacherDTO -> {
                TeacherDTO teacherForUserRole = new TeacherDTO();
                teacherForUserRole.setFirstName(teacherDTO.getFirstName());
                teacherForUserRole.setLastName(teacherDTO.getLastName());
                return teacherForUserRole;
            }).collect(Collectors.toList());
        } else {
            return teachers;
        }
    }

    @IsAdmin
    @GetMapping(path = "/teachers/sorted_by_age/{sortingOrder}")
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

    @IsAdmin
    @GetMapping(path = "/teachers/course/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public List<TeacherDTO> getAllTeachersAssignedToCourse(@PathVariable(name = "id") Integer id) {
        return teacherService.getAllTeachersAssignedToCourse(id);
    }

    @IsAdmin
    @DeleteMapping(path = "/teacher/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteTeacher(@PathVariable(name = "id") Integer id) {
        teacherService.deleteTeacher(id);
    }
}
