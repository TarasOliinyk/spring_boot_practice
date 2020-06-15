package com.springboot.practice.controller;

import com.springboot.practice.annotation.permission.teacher.*;
import com.springboot.practice.annotation.role.IsAdmin;
import com.springboot.practice.data.UserRole;
import com.springboot.practice.dto.TeacherDTO;
import com.springboot.practice.exceptions.teacher.IllegalTeacherSearchException;
import com.springboot.practice.service.TeacherService;
import com.springboot.practice.service.criteria.TeacherSortingCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @PostMapping(path = "/teacher")
    @HasTeacherCreatePermission
    public ResponseEntity<TeacherDTO> createTeacher(@RequestBody TeacherDTO teacherDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherService.createTeacher(teacherDTO));
    }

    @GetMapping(path = "/teacher/{id}")
    @HasTeacherReadPermission
    public ResponseEntity<TeacherDTO> getTeacher(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(teacherService.getTeacher(id));
    }

    @GetMapping(path = "/teacher/phone_number/{phoneNumber}")
    @RolesAllowed({UserRole.Name.ADMIN, UserRole.Name.TEACHER})
    public ResponseEntity<TeacherDTO> getTeacherByPhoneNumber(@PathVariable(name = "phoneNumber") String phoneNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(teacherService.getTeacherByPhoneNumber(phoneNumber));
    }

    @GetMapping(path = "/teacher/list")
    @ResponseStatus(HttpStatus.FOUND)
    @HasTeacherReadPermission
    public List<TeacherDTO> getAllTeachers(Authentication authentication) {
        UserRole userRole = UserRole.valueOf(authentication.getAuthorities().iterator().next().getAuthority());
        List<TeacherDTO> teachers = teacherService.getAllTeachers();

        if (userRole.equals(UserRole.ROLE_USER)) {
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

    @GetMapping(path = "/teachers/sorted_by_age/{sortingOrder}")
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed({UserRole.Name.ADMIN, UserRole.Name.TEACHER})
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

    @GetMapping(path = "/teachers/course/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    @IsAdmin
    public List<TeacherDTO> getAllTeachersAssignedToCourse(@PathVariable(name = "id") Integer id) {
        return teacherService.getAllTeachersAssignedToCourse(id);
    }

    @DeleteMapping(path = "/teacher/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @HasTeacherDeletePermission
    public void deleteTeacher(@PathVariable(name = "id") Integer id) {
        teacherService.deleteTeacher(id);
    }
}
