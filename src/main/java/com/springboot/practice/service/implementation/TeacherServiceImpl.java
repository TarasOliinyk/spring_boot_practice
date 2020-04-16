package com.springboot.practice.service.implementation;

import com.springboot.practice.exceptions.teacher.IllegalTeacherArgumentException;
import com.springboot.practice.exceptions.teacher.IllegalTeacherSearchException;
import com.springboot.practice.exceptions.teacher.TeacherNotFoundException;
import com.springboot.practice.model.Course;
import com.springboot.practice.model.Teacher;
import com.springboot.practice.repository.TeacherRepository;
import com.springboot.practice.service.TeacherService;
import com.springboot.practice.service.criteria.TeacherSortingCriteria;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public Teacher createTeacher(String firstName, String lastName, Integer age) {

        if (firstName != null && lastName != null && age != null && age > 0) {
            return teacherRepository.save(new Teacher(firstName, lastName, age));
        } else {
            throw new IllegalTeacherArgumentException();
        }
    }

    @Override
    public Teacher getTeacher(Integer teacherId) {
        return teacherRepository.findOneById(teacherId).orElseThrow(TeacherNotFoundException::new);
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public List<Teacher> getAllTeachersSortedBy(TeacherSortingCriteria sortingParameter) {
        Sort sort;

        switch (sortingParameter) {
            case ASCENDING_BY_AGE:
                sort = Sort.by(Sort.Order.asc("age"));
                break;
            case DESCENDING_BY_AGE:
                sort = Sort.by(Sort.Order.desc("age"));
                break;
            default:
                throw new IllegalTeacherSearchException("Unexpected value: " + sortingParameter);
        }
        return teacherRepository.findAll(sort);
    }

    @Override
    public List<Teacher> getAllTeachersAssignedToCourse(Course course) {
        return teacherRepository.findAllByCoursesContaining(course);
    }

    @Override
    public void deleteTeacher(Integer teacherId) {
        teacherRepository.deleteById(teacherId);
    }
}
