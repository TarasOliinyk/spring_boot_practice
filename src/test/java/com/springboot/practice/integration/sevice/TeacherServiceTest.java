package com.springboot.practice.integration.sevice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.springboot.practice.dto.CourseDTO;
import com.springboot.practice.dto.TeacherDTO;
import com.springboot.practice.model.Course;
import com.springboot.practice.model.Teacher;
import com.springboot.practice.repository.CourseRepository;
import com.springboot.practice.repository.TeacherRepository;
import com.springboot.practice.service.TeacherService;
import com.springboot.practice.service.criteria.TeacherSortingCriteria;
import com.springboot.practice.utils.json.JsonParser;
import com.springboot.practice.utils.json.supplier.teacher.TeacherJsonData;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TeacherServiceTest {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @MockBean // ToDo: Mocked for testing as a POC
    private TeacherRepository teacherRepository;
    @MockBean // ToDo: Mocked for testing as a POC
    private CourseRepository courseRepository;

    @Test
    public void createTeacher_CreateNewTeacher_ReturnCreatedTeacher() {
        Teacher teacher = JsonParser.buildObjectFromJSON(TeacherJsonData.TEACHER_FOR_CREATE_ONE_POSITIVE,
                new TypeReference<>() {});
        TeacherDTO expectedTeacherDTO = JsonParser.buildObjectFromJSON(TeacherJsonData.TEACHER_FOR_CREATE_ONE_POSITIVE,
                new TypeReference<>() {});

        Mockito.when(teacherRepository.save(eq(teacher))).thenReturn(teacher);

        TeacherDTO actualTeacherDTO = teacherService.createTeacher(expectedTeacherDTO);

        Assertions.assertThat(actualTeacherDTO)
                .as("Actual teacher returned by Teacher Service using 'createTeacher' method differs from " +
                        "the expected one")
                .isEqualTo(expectedTeacherDTO);
    }

    @Test
    public void findOneById_RetrieveTeacher_ReturnTeacher() {
        Teacher teacher = JsonParser.buildObjectFromJSON(TeacherJsonData.TEACHER_FOR_FIND_ONE_POSITIVE,
                new TypeReference<>() {});
        TeacherDTO expectedTeacherDTO = JsonParser.buildObjectFromJSON(TeacherJsonData.TEACHER_FOR_FIND_ONE_POSITIVE,
                new TypeReference<>() {});

        Mockito.when(teacherRepository.findOneById(eq(teacher.getId()))).thenReturn(Optional.of(teacher));

        TeacherDTO actualTeacherDTO = teacherService.getTeacher(teacher.getId());

        Assertions.assertThat(actualTeacherDTO)
                .as("Actual teacher returned by Teacher Service using 'getTeacher' method differs from the " +
                        "expected one")
                .isEqualTo(expectedTeacherDTO);
    }

    @Test
    public void findAll_RetrieveAllTeachers_ReturnTeachers() {
        List<Teacher> expectedTeachers = JsonParser.buildObjectFromJSON(TeacherJsonData.TEACHERS_FOR_FIND_ALL_POSITIVE,
                new TypeReference<>() {});

        Mockito.when(teacherRepository.findAll()).thenReturn(expectedTeachers);

        List<TeacherDTO> actualTeacherDTOs = teacherService.getAllTeachers();

        Assertions.assertThat(actualTeacherDTOs)
                .usingFieldByFieldElementComparator()
                .as("Actual teachers returned by Teacher Service using 'getAllTeachers' method differ from " +
                        "the expected ones")
                .isEqualTo(expectedTeachers);
    }

    @Test
    public void findAll_RetrieveTeachersSortedByAgeAscending_ReturnTeachers() {
        LinkedList<Teacher> expectedTeachers = JsonParser.buildObjectFromJSON(
                TeacherJsonData.TEACHERS_FOR_FIND_ALL_SORTED_BY_AGE_ASC, new TypeReference<>() {});

        Mockito.when(teacherRepository.findAll(eq(Sort.by(Sort.Order.asc("age"))))).thenReturn(expectedTeachers);

        List<TeacherDTO> actualTeacherDTOs = teacherService.getAllTeachersSortedBy(TeacherSortingCriteria.ASCENDING_BY_AGE);

        Assertions.assertThat(actualTeacherDTOs)
                .usingFieldByFieldElementComparator()
                .as("Actual teachers returned by Teacher Service using 'getAllTeachersSortedBy' method with " +
                        "parameter 'age' along with ascending sorting type differ from the expected ones")
                .isEqualTo(expectedTeachers);
    }

    @Test
    public void findAllByCoursesContaining_RetrieveAllTeachersAssignedToCourse_ReturnTeachers() {
        CourseDTO courseDTO = JsonParser.buildObjectFromJSON(TeacherJsonData.COURSE_FOR_FIND_ALL_BY_COURSES_CONTAINING,
                new TypeReference<>() {});
        Course course = JsonParser.buildObjectFromJSON(TeacherJsonData.COURSE_FOR_FIND_ALL_BY_COURSES_CONTAINING,
                new TypeReference<>() {});
        List<Teacher> expectedTeachers = JsonParser.buildObjectFromJSON(TeacherJsonData.TEACHERS_FOR_FIND_ALL_BY_COURSES_CONTAINING,
                new TypeReference<>() {});

        Mockito.when(courseRepository.findOneById(eq(course.getId()))).thenReturn(Optional.of(course));
        Mockito.when(teacherRepository.findAllByCoursesContaining(eq(course))).thenReturn(expectedTeachers);

        List<TeacherDTO> actualTeacherDTOs = teacherService.getAllTeachersAssignedToCourse(courseDTO.getId());

        Assertions.assertThat(actualTeacherDTOs)
                .usingFieldByFieldElementComparator()
                .as("Actual teacher returned by Teacher Service using 'getAllTeachersAssignedToCourse' method " +
                        "differ from the expected ones")
                .isEqualTo(expectedTeachers);
    }
}
