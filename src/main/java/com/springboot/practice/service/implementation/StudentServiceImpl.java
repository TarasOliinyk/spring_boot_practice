package com.springboot.practice.service.implementation;

import com.springboot.practice.dto.StudentDTO;
import com.springboot.practice.exceptions.student.StudentNotFoundException;
import com.springboot.practice.model.Student;
import com.springboot.practice.repository.StudentRepository;
import com.springboot.practice.service.StudentService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {
    private static Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    public StudentServiceImpl(StudentRepository studentRepository, ModelMapper modelMapper) {
        this.studentRepository = studentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        logger.info("Create student " + studentDTO.toString());
        return modelMapper.map(studentRepository.save(new Student(studentDTO.getFirstName(), studentDTO.getLastName(),
                studentDTO.getAge())), StudentDTO.class);
    }

    @Override
    public StudentDTO getStudent(Integer studentId) {
        logger.info("Get student with id " + studentId);
        return modelMapper.map(studentRepository.findOneById(studentId).orElseThrow(
                () -> new StudentNotFoundException("There is no student with id " + studentId)), StudentDTO.class);
    }

    @Override
    public StudentDTO updateStudent(StudentDTO studentDTO) {
        logger.info(String.format("Update student with id %s, updated student: %s", studentDTO.getId(), studentDTO.toString()));
        Student student = modelMapper.map(studentDTO, Student.class);
        return modelMapper.map(studentRepository.save(student), StudentDTO.class);
    }

    @Override
    public int getNumberOfCoursesAssignedToStudent(Integer studentId) {
        logger.info("Get number of courses assigned to student with id " + studentId);
        return getStudent(studentId).getCourses().size();
    }

    @Override
    public void deleteStudent(Integer studentId) {
        logger.info("Delete student with id " + studentId);
        studentRepository.deleteById(studentId);
    }
}
