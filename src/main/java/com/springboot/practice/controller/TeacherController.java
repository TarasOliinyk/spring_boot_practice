package com.springboot.practice.controller;

import com.springboot.practice.data.Role;
import com.springboot.practice.dto.TeacherDTO;
import com.springboot.practice.exceptions.teacher.IllegalTeacherSearchException;
import com.springboot.practice.service.TeacherService;
import com.springboot.practice.service.criteria.TeacherSortingCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TeacherController {
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

//    @IsAdmin // ToDo: Need to set corresponding permission
    @PostMapping(path = "/teacher")
    public ResponseEntity<TeacherDTO> createTeacher(@RequestBody TeacherDTO teacherDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherService.createTeacher(teacherDTO));
    }

//    @RolesAllowed({Role.Name.STUDENT, Role.Name.TEACHER, Role.Name.ADMIN}) // ToDo: Need to set corresponding permission
    @GetMapping(path = "/teacher/{id}")
    public ResponseEntity<TeacherDTO> getTeacher(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(teacherService.getTeacher(id));
    }

//    @IsAdmin // ToDo: Need to set corresponding permission
    @GetMapping(path = "/teacher/phone_number/{phoneNumber}")
    public ResponseEntity<TeacherDTO> getTeacherByPhoneNumber(@PathVariable(name = "phoneNumber") String phoneNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(teacherService.getTeacherByPhoneNumber(phoneNumber));
    }

//    @RolesAllowed({Role.Name.USER, Role.Name.STUDENT, Role.Name.TEACHER, Role.Name.ADMIN}) // ToDo: Need to set corresponding permission
    @GetMapping(path = "/teacher/list")
    @ResponseStatus(HttpStatus.FOUND)
    public List<TeacherDTO> getAllTeachers(Authentication authentication) {
        Role userRole = Role.valueOf(authentication.getAuthorities().iterator().next().getAuthority());
        List<TeacherDTO> teachers = teacherService.getAllTeachers();

        if (userRole.equals(Role.ROLE_USER)) { // ToDo: Should be modified in accordance with the new User Permissions approach
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

//    @IsAdmin // ToDo: Need to set corresponding permission
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

//    @IsAdmin // ToDo: Need to set corresponding permission
    @GetMapping(path = "/teachers/course/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public List<TeacherDTO> getAllTeachersAssignedToCourse(@PathVariable(name = "id") Integer id) {
        return teacherService.getAllTeachersAssignedToCourse(id);
    }

//    @IsAdmin // ToDo: Need to set corresponding permission
    @DeleteMapping(path = "/teacher/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteTeacher(@PathVariable(name = "id") Integer id) {
        teacherService.deleteTeacher(id);
    }
}
