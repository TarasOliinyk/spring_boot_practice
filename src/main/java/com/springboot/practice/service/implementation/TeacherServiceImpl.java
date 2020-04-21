package com.springboot.practice.service.implementation;

import com.springboot.practice.dto.CourseDTO;
import com.springboot.practice.dto.TeacherDTO;
import com.springboot.practice.exceptions.teacher.IllegalTeacherSearchException;
import com.springboot.practice.exceptions.teacher.TeacherNotFoundException;
import com.springboot.practice.model.Course;
import com.springboot.practice.model.Teacher;
import com.springboot.practice.repository.TeacherRepository;
import com.springboot.practice.service.TeacherService;
import com.springboot.practice.service.criteria.TeacherSortingCriteria;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;

    public TeacherServiceImpl(TeacherRepository teacherRepository, ModelMapper modelMapper) {
        this.teacherRepository = teacherRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TeacherDTO createTeacher(String firstName, String lastName, Integer age) {
        return modelMapper.map(teacherRepository.save(new Teacher(firstName, lastName, age)), TeacherDTO.class);
    }

    @Override
    public TeacherDTO getTeacher(Integer teacherId) {
        return modelMapper.map(teacherRepository.findOneById(teacherId).orElseThrow(TeacherNotFoundException::new),
                TeacherDTO.class);
    }

    @Override
    public List<TeacherDTO> getAllTeachers() {
        Type listType = new TypeToken<List<TeacherDTO>>(){}.getType();
        return modelMapper.map(teacherRepository.findAll(), listType);
    }

    @Override
    public List<TeacherDTO> getAllTeachersSortedBy(TeacherSortingCriteria sortingParameter) {
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
        Type listType = new TypeToken<List<TeacherDTO>>(){}.getType();
        return modelMapper.map(teacherRepository.findAll(sort), listType);
    }

    @Override
    public List<TeacherDTO> getAllTeachersAssignedToCourse(CourseDTO courseDTO) {
        Type listType = new TypeToken<List<TeacherDTO>>(){}.getType();
        Course course = modelMapper.map(courseDTO, Course.class);
        return modelMapper.map(teacherRepository.findAllByCoursesContaining(course), listType);
    }

    @Override
    public void deleteTeacher(Integer teacherId) {
        teacherRepository.deleteById(teacherId);
    }
}
