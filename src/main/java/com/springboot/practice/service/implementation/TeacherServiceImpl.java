package com.springboot.practice.service.implementation;

import com.springboot.practice.model.Course;
import com.springboot.practice.model.Teacher;
import com.springboot.practice.repository.TeacherRepository;
import com.springboot.practice.service.TeacherService;
import com.springboot.practice.service.criteria.TeacherSortingParameter;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

@Service
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
        return teacherRepository.findOneById(teacherId);
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public List<Teacher> getAllTeachersSortedBy(TeacherSortingParameter sortingParameter) {
        Sort sort;

        switch (sortingParameter) {
            case ASCENDING_BY_AGE:
                sort = Sort.by(Sort.Order.asc("age"));
                break;
            case DESCENDING_BY_AGE:
                sort = Sort.by(Sort.Order.desc("age"));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + sortingParameter);
        }
        return teacherRepository.findAll(sort);
    }

    @Override
    public Teacher getTeacherAssignedToCourse(Integer courseId) {
        Spliterator<Teacher> spliterator = Spliterators.spliteratorUnknownSize(teacherRepository.findAll().iterator(), 0);
        return StreamSupport.stream(spliterator, false).filter(
                teacher -> teacher.getCourses().stream().anyMatch(course -> course.getId().equals(courseId))).findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("There is no Teacher assigned to course with id '%s'", courseId)));
    }

    @Override
    public List<Course> getAllCoursesAssignedToTeacher(Teacher teacher) {
        return teacherRepository.findOneById(teacher.getId()).getCourses();
    }

    @Override
    public void deleteTeacher(Integer teacherId) {
        teacherRepository.deleteById(teacherId);
    }
}
