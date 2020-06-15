package com.springboot.practice.service;

import com.springboot.practice.dto.TeacherDTO;
import com.springboot.practice.service.criteria.TeacherSortingCriteria;

import java.util.List;

public interface TeacherService {

    TeacherDTO createTeacher(TeacherDTO teacherDTO);

    TeacherDTO getTeacher(Integer teacherId);

    TeacherDTO getTeacherByPhoneNumber(String phoneNumber);

    List<TeacherDTO> getAllTeachers();

    List<TeacherDTO> getAllTeachersSortedBy(TeacherSortingCriteria sortingParameter);

    List<TeacherDTO> getAllTeachersAssignedToCourse(Integer courseId);

    void deleteTeacher(Integer teacherId);
}
