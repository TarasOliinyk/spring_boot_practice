package com.springboot.practice.service.implementation;

import com.springboot.practice.model.Teacher;
import com.springboot.practice.repository.TeacherRepository;
import com.springboot.practice.service.TeacherService;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public Teacher createTeacher(String firstName, String lastName, Integer age) {
        return teacherRepository.save(new Teacher(firstName, lastName, age));
    }

    @Override
    public Teacher getTeacher(Integer teacherId) {
        return teacherRepository.findTeacherById(teacherId);
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAllTeachers();
    }

    @Override
    public List<Teacher> getAllTeachersSortedBy(Sort sort) {
        return teacherRepository.findAllTeachersSortedBy(sort);
    }

    @Override
    public Teacher getTeacherAssignedToCourse(Integer courseId) {
        Spliterator<Teacher> spliterator = Spliterators.spliteratorUnknownSize(teacherRepository.findAll().iterator(), 0);
        return StreamSupport.stream(spliterator, false).filter(
                teacher -> teacher.getCourses().stream().anyMatch(course -> course.getId().equals(courseId))).findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("There is no Teacher assigned to course with id '%s'", courseId)));
    }

    @Override
    public void deleteTeacher(Integer teacherId) {
        teacherRepository.deleteById(teacherId);
    }
}
