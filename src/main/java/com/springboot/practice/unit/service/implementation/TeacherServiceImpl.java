package com.springboot.practice.unit.service.implementation;

import com.springboot.practice.dto.CourseDTO;
import com.springboot.practice.dto.TeacherDTO;
import com.springboot.practice.exceptions.teacher.IllegalTeacherSearchException;
import com.springboot.practice.exceptions.teacher.TeacherNotFoundException;
import com.springboot.practice.model.Course;
import com.springboot.practice.model.Teacher;
import com.springboot.practice.repository.TeacherRepository;
import com.springboot.practice.unit.service.TeacherService;
import com.springboot.practice.unit.service.criteria.TeacherSortingCriteria;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    private static Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;

    public TeacherServiceImpl(TeacherRepository teacherRepository, ModelMapper modelMapper) {
        this.teacherRepository = teacherRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TeacherDTO createTeacher(String firstName, String lastName, Integer age) {
        logger.info(String.format("Create teacher with first name: '%s', last name '%s', age: '%s'", firstName, lastName,
                age));
        return modelMapper.map(teacherRepository.save(new Teacher(firstName, lastName, age)), TeacherDTO.class);
    }

    @Override
    public TeacherDTO getTeacher(Integer teacherId) {
        logger.info("Get teacher with id: " + teacherId);
        return modelMapper.map(teacherRepository.findOneById(teacherId).orElseThrow(TeacherNotFoundException::new),
                TeacherDTO.class);
    }

    @Override
    public List<TeacherDTO> getAllTeachers() {
        logger.info("Get all teachers");
        Type listType = new TypeToken<List<TeacherDTO>>(){}.getType();
        return modelMapper.map(teacherRepository.findAll(), listType);
    }

    @Override
    public List<TeacherDTO> getAllTeachersSortedBy(TeacherSortingCriteria sortingParameter) {
        logger.info("Get all courses sorted by " + sortingParameter.name());
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
        logger.info("Get all teachers assigned to course " + courseDTO.toString());
        Type listType = new TypeToken<List<TeacherDTO>>(){}.getType();
        Course course = modelMapper.map(courseDTO, Course.class);
        return modelMapper.map(teacherRepository.findAllByCoursesContaining(course), listType);
    }

    @Override
    public void deleteTeacher(Integer teacherId) {
        logger.info("Delete teacher with id: " + teacherId);
        teacherRepository.deleteById(teacherId);
    }
}
