package com.springboot.practice.unit.service.implementation;

import com.springboot.practice.dto.StudentDTO;
import com.springboot.practice.exceptions.teacher.StudentNotFoundException;
import com.springboot.practice.model.Student;
import com.springboot.practice.repository.StudentRepository;
import com.springboot.practice.unit.service.StudentService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {
    private static Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    public StudentServiceImpl(StudentRepository studentRepository, ModelMapper modelMapper) {
        this.studentRepository = studentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public StudentDTO createStudent(String firstName, String lastName, Integer age) {
        logger.info(String.format("Create student with first name: '%s', last name '%s', age: '%s'", firstName, lastName,
                age));
        return modelMapper.map(studentRepository.save(new Student(firstName, lastName, age)), StudentDTO.class);
    }

    @Override
    public StudentDTO getStudent(Integer studentId) {
        logger.info("Get student with id: " + studentId);
        return modelMapper.map(studentRepository.findOneById(studentId).orElseThrow(
                () -> new StudentNotFoundException("There is no student with id " + studentId)), StudentDTO.class);
    }

    @Override
    public StudentDTO updateStudent(StudentDTO studentDTO) {
        logger.info("Update student " + studentDTO.toString());
        Student student = modelMapper.map(studentDTO, Student.class);
        return modelMapper.map(studentRepository.save(student), StudentDTO.class);
    }

    @Override
    public void deleteStudent(Integer studentId) {
        logger.info("Delete student with id: " + studentId);
        studentRepository.deleteById(studentId);
    }
}
