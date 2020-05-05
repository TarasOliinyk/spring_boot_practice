package com.springboot.practice.unit.service;

import com.springboot.practice.dto.CourseDTO;
import com.springboot.practice.dto.TeacherDTO;
import com.springboot.practice.unit.service.criteria.TeacherSortingCriteria;

import java.util.List;

public interface TeacherService {

    TeacherDTO createTeacher(String firstName, String lastName, Integer age);

    TeacherDTO getTeacher(Integer teacherId);

    TeacherDTO getTeacherByPhoneNumber(String phoneNumber);

    List<TeacherDTO> getAllTeachers();

    List<TeacherDTO> getAllTeachersSortedBy(TeacherSortingCriteria sortingParameter);

    List<TeacherDTO> getAllTeachersAssignedToCourse(CourseDTO courseDTO);

    void deleteTeacher(Integer teacherId);
}
