package com.springboot.practice.unit.service.implementation;

import com.springboot.practice.dto.CourseDTO;
import com.springboot.practice.dto.StudentDTO;
import com.springboot.practice.dto.TeacherDTO;
import com.springboot.practice.exceptions.course.CourseNotFoundException;
import com.springboot.practice.exceptions.course.IllegalCourseArgumentException;
import com.springboot.practice.exceptions.course.IllegalCourseSearchException;
import com.springboot.practice.model.Course;
import com.springboot.practice.model.Student;
import com.springboot.practice.model.Teacher;
import com.springboot.practice.repository.CourseRepository;
import com.springboot.practice.unit.service.CourseService;
import com.springboot.practice.unit.service.criteria.CourseCriteria;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    private static Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;

    public CourseServiceImpl(CourseRepository courseRepository, ModelMapper modelMapper) {
        this.courseRepository = courseRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CourseDTO createCourse(String courseName) {
        logger.info(String.format("Create course with name '%s'", courseName));

        if (courseName != null) {
            return modelMapper.map(courseRepository.save(new Course(courseName)), CourseDTO.class);
        } else {
            throw new IllegalCourseArgumentException();
        }
    }

    @Override
    public CourseDTO createCourseWithStartAndEndDates(String courseName, LocalDate startDate, LocalDate endDate) {
        logger.info(String.format("Create course with name '%s', start date '%s', end date '%s'", courseName,
                startDate, endDate));
        return modelMapper.map(courseRepository.save(new Course(courseName, startDate, endDate)), CourseDTO.class);
    }

    @Override
    public CourseDTO updateCourse(CourseDTO courseDTO) {
        logger.info(String.format("Update course with id %s, updated course: %s", courseDTO.getId(), courseDTO.toString()));
        Course course = modelMapper.map(courseDTO, Course.class);
        return modelMapper.map(courseRepository.save(course), CourseDTO.class);
    }

    @Override
    public CourseDTO getCourse(Integer id) {
        logger.info("Retrieve course with id " + id);
        return modelMapper.map(courseRepository.findOneById(id).orElseThrow(CourseNotFoundException::new), CourseDTO.class);
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        logger.info("Get all courses");
        Type listType = new TypeToken<List<CourseDTO>>(){}.getType();
        return modelMapper.map(courseRepository.findAll(), listType);
    }

    @Override
    public CourseDTO assignTeacherToCourse(CourseDTO courseDTO, TeacherDTO teacherDTO) {
        logger.info(String.format("Assign teacher (id = %s) to course (id = %s)", teacherDTO.getId(), courseDTO.getId()));
        Course course = modelMapper.map(courseDTO, Course.class);
        Teacher teacher = modelMapper.map(teacherDTO, Teacher.class);
        course.getTeachers().add(teacher);
        return updateCourse(modelMapper.map(course, CourseDTO.class));
    }

    @Override
    public CourseDTO assignStudentToCourse(CourseDTO courseDTO, StudentDTO studentDTO) {
        logger.info(String.format("Assign student (id = %s) to course (id = %s)", studentDTO.getId(), courseDTO.getId()));
        Course course = modelMapper.map(courseDTO, Course.class);
        Student student = modelMapper.map(studentDTO, Student.class);
        course.getStudents().add(student);
        return updateCourse(modelMapper.map(course, CourseDTO.class));
    }

    @Override
    public CourseDTO unassignTeacherFromCourse(CourseDTO courseDTO, TeacherDTO teacherDTO) {
        logger.info(String.format("Unassign teacher (id = %s) from course (id = %s)", teacherDTO.getId(), courseDTO.getId()));
        Course course = modelMapper.map(courseDTO, Course.class);
        Teacher teacher = modelMapper.map(teacherDTO, Teacher.class);
        course.getTeachers().remove(teacher);
        return updateCourse(modelMapper.map(course, CourseDTO.class));
    }

    @Override
    public CourseDTO unassignStudentFromCourse(CourseDTO courseDTO, StudentDTO studentDTO) {
        logger.info(String.format("Unassign student (id = %s) from course (id = %s)", studentDTO.getId(), courseDTO.getId()));
        Course course = modelMapper.map(courseDTO, Course.class);
        Student student = modelMapper.map(studentDTO, Student.class);
        course.getStudents().remove(student);
        return updateCourse(modelMapper.map(course, CourseDTO.class));
    }

    @Override
    public List<CourseDTO> getCoursesWithNumberOfAssignedTeachers(int numberOfTeachers) {
        logger.info(String.format("Get courses with %s assigned teacher/s", numberOfTeachers));
        Type listType = new TypeToken<List<CourseDTO>>(){}.getType();
        return modelMapper.map(courseRepository.findAllByTeachersCount(numberOfTeachers), listType);
//        return getAllCourses().stream().filter(course -> course.getTeachers().size() == numberOfTeachers)
//                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDTO> getFilteredCourses(CourseCriteria criteria) {
        logger.info("Get courses filtered by " + criteria.name());
        Type listType = new TypeToken<List<CourseDTO>>(){}.getType();
        List<CourseDTO> filteredCourses;

        switch (criteria) {
            case NOT_STARTED:
                filteredCourses = modelMapper.map(courseRepository.findAllByStartDateGreaterThan(LocalDate.now()), listType);
                break;
            case FINISHED:
                filteredCourses = modelMapper.map(courseRepository.findAllByEndDateLessThan(LocalDate.now()), listType);
                break;
            case ONGOING:
                filteredCourses = modelMapper.map(courseRepository.findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate.now(), LocalDate.now()), listType);
                break;
            default:
                throw new IllegalCourseSearchException("Unexpected value: " + criteria);
        }
        return filteredCourses;
    }

    @Override
    public List<CourseDTO> getCoursesThatLast(int numberOfDays) {
        logger.info(String.format("Get courses that last %s day/s", numberOfDays));
        Type listType = new TypeToken<List<CourseDTO>>(){}.getType();
        return modelMapper.map(courseRepository.findAllByDateDiffBetweenStartDateAndEndDateEqualTo(numberOfDays), listType);
//        return getAllCourses().stream().filter(
//                course -> ChronoUnit.DAYS.between(course.getStartDate(), course.getEndDate()) == numberOfDays)
//                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDTO> getAllCoursesAssignedToTeacher(TeacherDTO teacherDTO) {
        logger.info("Get all courses assigned to teacher with id " + teacherDTO.getId());
        Teacher teacher = modelMapper.map(teacherDTO, Teacher.class);
        Type listType = new TypeToken<List<CourseDTO>>(){}.getType();
        return modelMapper.map(courseRepository.findAllByTeachersContaining(teacher), listType);
    }

    @Override
    public List<CourseDTO> getAllCoursesAssignedToStudent(StudentDTO studentDTO) {
        logger.info("Get all courses assigned to student with id " + studentDTO.getId());
        Student student = modelMapper.map(studentDTO, Student.class);
        Type listType = new TypeToken<List<CourseDTO>>(){}.getType();
        return modelMapper.map(courseRepository.findAllByStudentsContaining(student), listType);
    }

    @Override
    public List<CourseDTO> getAllCoursesWithAssignedTeacherAndStudent(TeacherDTO teacherDTO, StudentDTO studentDTO) {
        logger.info(String.format("Get all courses with assigned teacher (id = %s) and student (id = %s)",
                teacherDTO.getId(), studentDTO.getId()));
        Teacher teacher = modelMapper.map(teacherDTO, Teacher.class);
        Student student = modelMapper.map(studentDTO, Student.class);
        Type listType = new TypeToken<List<CourseDTO>>(){}.getType();
        return modelMapper.map(courseRepository.findAllByTeachersContainingAndStudentsContaining(teacher, student), listType);
    }

    @Override
    public void deleteCourse(Integer courseId) {
        logger.info("Delete course with id " + courseId);
        courseRepository.deleteById(courseId);
    }
}
