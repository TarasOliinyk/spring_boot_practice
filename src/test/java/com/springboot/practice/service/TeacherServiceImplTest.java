package com.springboot.practice.service;

import com.springboot.practice.dto.CourseDTO;
import com.springboot.practice.dto.TeacherDTO;
import com.springboot.practice.model.Course;
import com.springboot.practice.model.Teacher;
import com.springboot.practice.repository.TeacherRepository;
import com.springboot.practice.service.criteria.TeacherSortingCriteria;
import com.springboot.practice.service.implementation.TeacherServiceImpl;
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
import java.util.stream.Collectors;

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
        String firstName = "Mike";
        String lastName = "Brown";
        int age = 54;

        Teacher teacher = new Teacher(firstName, lastName, age);

        TeacherDTO expectedTeacherDTO = new TeacherDTO();
        expectedTeacherDTO.setFirstName(teacher.getFirstName());
        expectedTeacherDTO.setLastName(teacher.getLastName());
        expectedTeacherDTO.setAge(teacher.getAge());

        Mockito.when(teacherRepository.save(eq(teacher))).thenReturn(teacher);

        TeacherDTO actualTeacherDTO = teacherService.createTeacher(firstName, lastName, age);

        Assertions.assertThat(actualTeacherDTO)
                .as("Actual teacher created by Teacher Service using 'createTeacher' method differs from " +
                        "the expected one")
                .isEqualTo(expectedTeacherDTO);
    }

    @Test
    public void getTeacher_RetrieveTeacher_ReturnTeacher() {
        String fistName = "John";
        String lastName = "Travolta";
        int age = 45;

        Mockito.when(teacherRepository.findOneById(eq(4))).thenReturn(Optional.of(new Teacher(fistName, lastName, age)));

        TeacherDTO actualTeacherDTO = teacherService.getTeacher(4);

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(actualTeacherDTO.getFirstName())
                .as("Actual first name of the Teacher created by Teacher Service differs from the expected one")
                .isEqualTo(fistName);
        softAssertions.assertThat(actualTeacherDTO.getLastName())
                .as("Actual last name of the Teacher created by Teacher Service differs from the expected one")
                .isEqualTo(lastName);
        softAssertions.assertThat(actualTeacherDTO.getAge())
                .as("Actual age of the Teacher created by Teacher Service differs from the expected one")
                .isEqualTo(age);
        softAssertions.assertAll();
    }

    @Test
    public void getAllTeachers_RetrieveAllTeachers_ReturnTeachers() {
        List<Teacher> teachers = Arrays.asList(new Teacher("Chris", "White", 32),
                new Teacher("Nicolas", "Green", 43));

        List<TeacherDTO> expectedTeacherDTOs = new ArrayList<>();

        teachers.forEach(teacher -> {
            TeacherDTO teacherDTO = new TeacherDTO();
            teacherDTO.setFirstName(teacher.getFirstName());
            teacherDTO.setLastName(teacher.getLastName());
            teacherDTO.setAge(teacher.getAge());
            expectedTeacherDTOs.add(teacherDTO);
        });

        Mockito.when(teacherRepository.findAll()).thenReturn(teachers);

        List<TeacherDTO> actualTeacherDTOs = teacherService.getAllTeachers();

        Assertions.assertThat(actualTeacherDTOs)
                .as("Actual list of Teachers retrieved by Teacher Service using 'getAllTeachers' method " +
                        "differs form the expected one")
                .isEqualTo(expectedTeacherDTOs);
    }

    @Test
    public void getAllTeachersSortedBy_RetrieveTeachersSortedByAge_ReturnTeachers() {
        Teacher teacherA = new Teacher("Bill", "Clinton", 56);
        Teacher teacherB = new Teacher("Barack", "Obama", 34);
        Teacher teacherC = new Teacher("Donald", "Trump", 25);
        LinkedList<Teacher> teachers = new LinkedList<>(Arrays.asList(teacherC, teacherB, teacherA));

        Mockito.when(teacherRepository.findAll(eq(Sort.by(Sort.Order.asc("age"))))).thenReturn(teachers);

        List<TeacherDTO> actualTeacherDTOs = teacherService.getAllTeachersSortedBy(TeacherSortingCriteria.ASCENDING_BY_AGE);
        List<Integer> actualTeachersAge = actualTeacherDTOs.stream().map(TeacherDTO::getAge).collect(Collectors.toList());
        List<Integer> expectedTeachersAge = teachers.stream().map(Teacher::getAge).collect(Collectors.toList());

        Assertions.assertThat(actualTeachersAge)
                .as("Actual list of teachers' age returned by Teacher Service using 'getAllTeachersSortedBy' " +
                        "method differs from the expected one")
                .isEqualTo(expectedTeachersAge);
    }

    @Test
    public void getAllTeachersAssignedToCourse_RetrieveTeachersOfCourse_ReturnTeachers() {
        Course course = new Course("Math");
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setName(course.getName());

        Teacher teacherA = new Teacher("Tom", "Brown", 34);
        Teacher teacherB = new Teacher("Mike", "White", 56);
        Teacher teacherC = new Teacher("Nick", "Green", 28);
        List<Teacher> teachers = Arrays.asList(teacherA, teacherB, teacherC);

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
