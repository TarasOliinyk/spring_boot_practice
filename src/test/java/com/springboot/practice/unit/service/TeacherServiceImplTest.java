package com.springboot.practice.unit.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.springboot.practice.dto.CourseDTO;
import com.springboot.practice.dto.TeacherDTO;
import com.springboot.practice.model.Course;
import com.springboot.practice.model.Teacher;
import com.springboot.practice.repository.TeacherRepository;
import com.springboot.practice.unit.service.criteria.TeacherSortingCriteria;
import com.springboot.practice.unit.service.implementation.TeacherServiceImpl;
import com.springboot.practice.utils.json.JsonParser;
import com.springboot.practice.utils.json.supplier.teacher.TeacherJsonData;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

import static org.mockito.ArgumentMatchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class TeacherServiceImplTest {
    private TeacherService teacherService;
    @Mock
    private TeacherRepository teacherRepository;
    private ModelMapper modelMapper = new ModelMapper();

    @Before
    public void init() {
        teacherService = new TeacherServiceImpl(teacherRepository, modelMapper);
    }

    // Positive scenarios:

    @Test
    public void createTeacher_CreateNewTeacher_ReturnTeacher() {
        Teacher teacher = JsonParser.buildObjectFromJSON(TeacherJsonData.TEACHER_FOR_CREATE_ONE_POSITIVE,
                new TypeReference<>() {});
        TeacherDTO expectedTeacherDTO = JsonParser.buildObjectFromJSON(TeacherJsonData.TEACHER_FOR_CREATE_ONE_POSITIVE,
                new TypeReference<>() {});

        Mockito.when(teacherRepository.save(eq(teacher))).thenReturn(teacher);

        TeacherDTO actualTeacherDTO = teacherService.createTeacher(teacher.getFirstName(), teacher.getLastName(),
                teacher.getAge());

        Assertions.assertThat(actualTeacherDTO)
                .as("Actual teacher created by Teacher Service using 'createTeacher' method differs from " +
                        "the expected one")
                .isEqualTo(expectedTeacherDTO);
    }

    @Test
    public void getTeacher_RetrieveTeacher_ReturnTeacher() {
        Teacher teacher = JsonParser.buildObjectFromJSON(TeacherJsonData.TEACHER_FOR_FIND_ONE_POSITIVE, new TypeReference<>() {});

        Mockito.when(teacherRepository.findOneById(eq(teacher.getId()))).thenReturn(Optional.of(teacher));

        TeacherDTO actualTeacherDTO = teacherService.getTeacher(teacher.getId());

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(actualTeacherDTO.getFirstName())
                .as("Actual first name of the Teacher created by Teacher Service differs from the expected one")
                .isEqualTo(teacher.getFirstName());
        softAssertions.assertThat(actualTeacherDTO.getLastName())
                .as("Actual last name of the Teacher created by Teacher Service differs from the expected one")
                .isEqualTo(teacher.getLastName());
        softAssertions.assertThat(actualTeacherDTO.getAge())
                .as("Actual age of the Teacher created by Teacher Service differs from the expected one")
                .isEqualTo(teacher.getAge());
        softAssertions.assertAll();
    }

    @Test
    public void getAllTeachers_RetrieveAllTeachers_ReturnTeachers() {
        List<Teacher> teachers = JsonParser.buildObjectFromJSON(TeacherJsonData.TEACHERS_FOR_FIND_ALL_POSITIVE,
                new TypeReference<>() {});
        List<TeacherDTO> expectedTeacherDTOs = JsonParser.buildObjectFromJSON(TeacherJsonData.TEACHERS_FOR_FIND_ALL_POSITIVE,
                new TypeReference<>() {});

        Mockito.when(teacherRepository.findAll()).thenReturn(teachers);

        List<TeacherDTO> actualTeacherDTOs = teacherService.getAllTeachers();

        Assertions.assertThat(actualTeacherDTOs)
                .as("Actual list of Teachers retrieved by Teacher Service using 'getAllTeachers' method " +
                        "differs form the expected one")
                .isEqualTo(expectedTeacherDTOs);
    }

    @Test
    public void getAllTeachersSortedBy_RetrieveTeachersSortedByAge_ReturnTeachers() {
        LinkedList<Teacher> teachers = JsonParser.buildObjectFromJSON(TeacherJsonData.TEACHERS_FOR_FIND_ALL_SORTED_BY_AGE_ASC,
                new TypeReference<>() {});
        List<TeacherDTO> expectedTeacherDTOs = JsonParser.buildObjectFromJSON(TeacherJsonData.TEACHERS_FOR_FIND_ALL_SORTED_BY_AGE_ASC,
                new TypeReference<>() {});

        Mockito.when(teacherRepository.findAll(eq(Sort.by(Sort.Order.asc("age"))))).thenReturn(teachers);

        List<TeacherDTO> actualTeacherDTOs = teacherService.getAllTeachersSortedBy(TeacherSortingCriteria.ASCENDING_BY_AGE);

        Assertions.assertThat(actualTeacherDTOs)
                .as("Actual list of teachers returned by Teacher Service using 'getAllTeachersSortedBy' " +
                        "method differs from the expected one")
                .isEqualTo(expectedTeacherDTOs);
    }

    @Test
    public void getAllTeachersAssignedToCourse_RetrieveTeachersOfCourse_ReturnTeachers() {
        Course course = JsonParser.buildObjectFromJSON(TeacherJsonData.COURSE_FOR_FIND_ALL_BY_COURSES_CONTAINING,
                new TypeReference<>() {});
        CourseDTO courseDTO = JsonParser.buildObjectFromJSON(TeacherJsonData.COURSE_FOR_FIND_ALL_BY_COURSES_CONTAINING,
                new TypeReference<>() {});
        List<Teacher> teachers = JsonParser.buildObjectFromJSON(TeacherJsonData.TEACHERS_FOR_FIND_ALL_BY_COURSES_CONTAINING,
                new TypeReference<>() {});

        Mockito.when(teacherRepository.findAllByCoursesContaining(eq(course))).thenReturn(teachers);
        
        List<TeacherDTO> actualTeacherDTOs = teacherService.getAllTeachersAssignedToCourse(courseDTO);

        Assertions.assertThat(actualTeacherDTOs)
                .as("Actual number of Teachers returned by Course Service using " +
                        "'getAllTeachersAssignedToCourse' method differs form the expected one")
                .hasSize(teachers.size())
                .usingFieldByFieldElementComparator()
                .as("Actual Teachers returned by Course Service using 'getAllTeachersAssignedToCourse' " +
                        "method differs form the expected one")
                .isEqualTo(teachers);
    }

    @Test
    public void deleteTeacher_RemoveSpecificTeacher() {
        teacherService.deleteTeacher(7);

        Mockito.verify(teacherRepository, Mockito.times(1)).deleteById(Mockito.eq(7));
    }

    // Negative scenarios:

    @Test
    public void createTeacher_CreateTeacherWithEmptyFirstName_TriggerValidation() {
        Teacher teacherToTest = new Teacher("", "Brown", 45);
        String expectedValidationMessage = "Teacher cannot be registered without first name";

        teacherFieldValidationTest(teacherToTest, expectedValidationMessage);
    }

    @Test
    public void createTeacher_CreateTeacherWithEmptyLastName_TriggerValidation() {
        Teacher teacherToTest = new Teacher("Mark", "", 54);
        String expectedValidationMessage = "Teacher cannot be registered without last name";

        teacherFieldValidationTest(teacherToTest, expectedValidationMessage);
    }

    @Test
    public void createTeacher_CreateTeacherYoungerThan16YearsOld_TriggerValidation() {
        Teacher teacherToTest = new Teacher("Jim", "Nelson", 14);
        String expectedValidationMessage = "Teacher has to be at least 16 years old";

        teacherFieldValidationTest(teacherToTest, expectedValidationMessage);
    }

//======================================================================================================================

    /**
     * Test method responsible for verification of fields with applied constraints belonging to a Teacher object.
     *
     * @param teacher                   - Teacher object with a field expected to be validated
     * @param expectedValidationMessage - Expected validation message
     */
    private void teacherFieldValidationTest(Teacher teacher, String expectedValidationMessage) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);

        Assertions.assertThat(violations.size())
                .as("Expected field validation has not been initiated")
                .isEqualTo(1);
        Assertions.assertThat(violations.iterator().next().getMessage())
                .as("Actual validation message differs from the expected one")
                .isEqualTo(expectedValidationMessage);
    }
}
