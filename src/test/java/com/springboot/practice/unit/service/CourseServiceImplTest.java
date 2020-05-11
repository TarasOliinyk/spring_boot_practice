package com.springboot.practice.unit.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.springboot.practice.dto.CourseDTO;
import com.springboot.practice.dto.TeacherDTO;
import com.springboot.practice.exceptions.course.IllegalCourseArgumentException;
import com.springboot.practice.model.Course;
import com.springboot.practice.model.Teacher;
import com.springboot.practice.repository.CourseRepository;
import com.springboot.practice.repository.StudentRepository;
import com.springboot.practice.repository.TeacherRepository;
import com.springboot.practice.unit.service.criteria.CourseCriteria;
import com.springboot.practice.unit.service.implementation.CourseServiceImpl;
import com.springboot.practice.utils.json.JsonParser;
import com.springboot.practice.utils.json.supplier.course.CourseJsonData;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CourseServiceImplTest {
    private CourseService courseService;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private StudentRepository studentRepository;
    private ModelMapper modelMapper = new ModelMapper();

    @Before
    public void init() {
        courseService = new CourseServiceImpl(courseRepository, teacherRepository, studentRepository, modelMapper);
    }

    // Positive scenarios:

    @Test
    public void createCourse_CreateNewCourse_ReturnCreatedCourse() {
        Course course = JsonParser.buildObjectFromJSON(CourseJsonData.COURSE_FOR_CREATE_ONE_POSITIVE, new TypeReference<>() {});
        CourseDTO expectedCourseDTO = JsonParser.buildObjectFromJSON(CourseJsonData.COURSE_FOR_CREATE_ONE_POSITIVE,
                new TypeReference<>() {});

        Mockito.when(courseRepository.save(eq(course))).thenReturn(course);

        CourseDTO actualCourseDTO = courseService.createCourse(course.getName());

        Assertions.assertThat(actualCourseDTO.getName())
                .as("Actual name of a course created by the Course Service differs from the expected one")
                .isEqualTo(expectedCourseDTO.getName());
    }

    @Test
    public void createCourseWithStartAndEndDates_CreateNewCourseWithStartAndEndDates_ReturnCreatedCourse() {
        Course course = JsonParser.buildObjectFromJSON(CourseJsonData.COURSE_FOR_CREATE_ONE_WITH_DATES_POSITIVE,
                new TypeReference<>() {});
        CourseDTO expectedCourseDTO = JsonParser.buildObjectFromJSON(CourseJsonData.COURSE_FOR_CREATE_ONE_WITH_DATES_POSITIVE,
                new TypeReference<>() {});

        Mockito.when(courseRepository.save(eq(course))).thenReturn(course);

        CourseDTO actualCourseDTO = courseService.createCourseWithStartAndEndDates(course.getName(), course.getStartDate(),
                course.getEndDate());

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(actualCourseDTO.getName())
                .as("Actual name of a course created by the Course Service differs from the expected one")
                .isEqualTo(expectedCourseDTO.getName());
        softAssertions.assertThat(actualCourseDTO.getStartDate())
                .as("Actual start date of a course created by the Course Service differs from the expected one")
                .isEqualTo(expectedCourseDTO.getStartDate());
        softAssertions.assertThat(actualCourseDTO.getEndDate())
                .as("Actual end date of a course created by the Course Service differs from the expected one")
                .isEqualTo(expectedCourseDTO.getEndDate());
        softAssertions.assertAll();
    }

    @Test
    public void updateCourse_UpdateSpecificCourse_ReturnUpdatedCourse() {
        Course course = JsonParser.buildObjectFromJSON(CourseJsonData.COURSE_FOR_UPDATE_COURSE, new TypeReference<>() {});
        String newCourseName = course.getName() + "Updated";
        Course courseWithUpdatedName = JsonParser.buildObjectFromJSON(CourseJsonData.COURSE_FOR_UPDATE_COURSE,
                new TypeReference<>() {});
        courseWithUpdatedName.setName(newCourseName);

        Mockito.when(courseRepository.save(eq(course))).thenReturn(courseWithUpdatedName);

        CourseDTO courseDTO = JsonParser.buildObjectFromJSON(CourseJsonData.COURSE_FOR_UPDATE_COURSE, new TypeReference<>() {});

        CourseDTO actualCourseDTO = courseService.updateCourse(courseDTO);

        Assertions.assertThat(actualCourseDTO)
                .as("Course updated by the Course Service has not been updated as expected")
                .isEqualToComparingFieldByField(courseWithUpdatedName);
    }

    @Test
    public void getCourse_RetrieveCourse_ReturnCourse() {
        Course course = JsonParser.buildObjectFromJSON(CourseJsonData.COURSE_FOR_FIND_ONE_POSITIVE,
                new TypeReference<>() {});
        CourseDTO expectedCourseDTO = JsonParser.buildObjectFromJSON(CourseJsonData.COURSE_FOR_FIND_ONE_POSITIVE,
                new TypeReference<>() {});

        Mockito.when(courseRepository.findOneById(eq(course.getId()))).thenReturn(Optional.of(course));

        CourseDTO actualCourseDTO = courseService.getCourse(course.getId());

        Assertions.assertThat(actualCourseDTO)
                .as("Actual course retrieved by Course Service differs from the expected one")
                .isEqualTo(expectedCourseDTO);
    }

    @Test
    public void getAllCourses_RetrieveAllCourses_ReturnCourses() {
        List<Course> courses = JsonParser.buildObjectFromJSON(CourseJsonData.COURSES_FOR_FIND_ALL_POSITIVE,
                new TypeReference<>() {});
        List<CourseDTO> expectedDTOs = JsonParser.buildObjectFromJSON(CourseJsonData.COURSES_FOR_FIND_ALL_POSITIVE,
                new TypeReference<>() {});

        Mockito.when(courseRepository.findAll()).thenReturn(courses);

        List<CourseDTO> actualDTOs = courseService.getAllCourses();

        Assertions.assertThat(actualDTOs)
                .as("Courses retrieved by Course Service using 'getAllCourses' method differ from the " +
                        "expected courses")
                .isEqualTo(expectedDTOs);
    }

    @Test
    public void getAllCoursesAssignedToTeacher_RetrieveCoursesOfTeacher_ReturnListOfCourses() {
        Teacher teacher = JsonParser.buildObjectFromJSON(CourseJsonData.TEACHER_FOR_FIND_ALL_BY_TEACHERS_CONTAINING,
                new TypeReference<>() {});
        TeacherDTO teacherDTO = JsonParser.buildObjectFromJSON(CourseJsonData.TEACHER_FOR_FIND_ALL_BY_TEACHERS_CONTAINING,
                new TypeReference<>() {});
        List<Course> expectedCourses = JsonParser.buildObjectFromJSON(CourseJsonData.COURSES_FOR_FIND_ALL_BY_TEACHERS_CONTAINING,
                new TypeReference<>() {});

        Mockito.when(teacherRepository.findOneById(teacher.getId())).thenReturn(Optional.of(teacher));
        Mockito.when(courseRepository.findAllByTeachersContaining(eq(teacher))).thenReturn(expectedCourses);

        List<CourseDTO> actualCourses = courseService.getAllCoursesAssignedToTeacher(teacherDTO.getId());

        Assertions.assertThat(actualCourses)
                .usingFieldByFieldElementComparator()
                .as("Actual courses retrieved by Course Service using 'getAllCoursesAssignedToTeacher' " +
                        "method differ from the expected ones")
                .isEqualTo(expectedCourses);
    }

    @Test
    public void assignTeacherToCourse_SetTeachersToCourse_ReturnCourse() {
        Teacher teacher = JsonParser.buildObjectFromJSON(CourseJsonData.TEACHER_FOR_ASSIGN_TEACHER_TO_COURSE,
                new TypeReference<>() {});
        Course course = JsonParser.buildObjectFromJSON(CourseJsonData.COURSE_FOR_ASSIGN_TEACHER_TO_COURSE,
                new TypeReference<>() {});

        Mockito.when(courseRepository.findOneById(eq(course.getId()))).thenReturn(Optional.of(course));
        Mockito.when(teacherRepository.findOneById(eq(teacher.getId()))).thenReturn(Optional.of(teacher));
        Mockito.when(courseRepository.save(eq(course))).thenReturn(course);

        CourseDTO actualCourseDTO = courseService.assignTeacherToCourse(course.getId(), teacher.getId());

        Assertions.assertThat(actualCourseDTO.getTeachers().size())
                .as("Course has not been updated by Course Service using 'assignTeacherToCourse' method")
                .isEqualTo(1);
        Assertions.assertThat(actualCourseDTO.getTeachers().get(0))
                .as("Expected teacher has not been assigned to course by Course Service using " +
                        "'assignTeacherToCourse' method")
                .isEqualToComparingFieldByField(teacher);
    }

    @Test
    public void unassignTeacherFromCourse_RemoveTeacherFromCourse_ReturnCourse() {
        List<Teacher> courseTeachers = JsonParser.buildObjectFromJSON(CourseJsonData.TEACHERS_FOR_UNASSIGN_TEACHER_FROM_COURSE,
                new TypeReference<>() {});
        List<TeacherDTO> courseTeacherDTOs = JsonParser.buildObjectFromJSON(CourseJsonData.TEACHERS_FOR_UNASSIGN_TEACHER_FROM_COURSE,
                new TypeReference<>() {});

        Course course = JsonParser.buildObjectFromJSON(CourseJsonData.COURSE_FOR_UNASSIGN_TEACHER_FROM_COURSE,
                new TypeReference<>() {});
        course.getTeachers().add(courseTeachers.get(1));

        CourseDTO courseDTO = JsonParser.buildObjectFromJSON(CourseJsonData.COURSE_FOR_UNASSIGN_TEACHER_FROM_COURSE,
                new TypeReference<>() {});
        courseDTO.getTeachers().addAll(courseTeacherDTOs);

        Teacher firstTeacher = courseTeachers.get(0);
        TeacherDTO firstTeacherDTO = courseTeacherDTOs.get(0);

        Mockito.when(courseRepository.findOneById(eq(course.getId()))).thenReturn(Optional.of(course));
        Mockito.when(teacherRepository.findOneById(eq(firstTeacher.getId()))).thenReturn(Optional.of(firstTeacher));
        Mockito.when(courseRepository.save(eq(course))).thenReturn(course);

        CourseDTO actualCourseDTO = courseService.unassignTeacherFromCourse(courseDTO.getId(), firstTeacherDTO.getId());

        Assertions.assertThat(actualCourseDTO.getTeachers().size())
                .as("Course has not been updated by Course Service using 'unassignTeacherFromCourse' method")
                .isEqualTo(courseTeachers.size() - 1);
        Assertions.assertThat(actualCourseDTO.getTeachers().get(0))
                .as("Wrong teacher has been unassigned from course by Course Service using " +
                        "'unassignTeacherFromCourse' method")
                .isEqualToComparingFieldByField(courseTeachers.get(1));
    }

    @Test
    public void getCoursesWithNumberOfAssignedTeachers_RetrieveCoursesWithSpecificNumberOfTeachers_ReturnCourses() {
        List<Course> courses = JsonParser.buildObjectFromJSON(CourseJsonData.COURSES_FOR_FIND_ALL_BY_TEACHERS_COUNT,
                new TypeReference<>() {});

        courses.get(0).getTeachers().addAll(Arrays.asList(new Teacher(), new Teacher(), new Teacher(), new Teacher()));
        courses.get(1).getTeachers().addAll(Arrays.asList(new Teacher(), new Teacher(), new Teacher()));
        courses.get(2).getTeachers().addAll(Arrays.asList(new Teacher(), new Teacher(), new Teacher(), new Teacher()));

        List<Course> expectedCourses = courses.stream().filter(course -> course.getTeachers().size() == 4)
                .collect(Collectors.toList());
        Type listType = new TypeToken<List<CourseDTO>>(){}.getType();
        List<CourseDTO> expectedCourseDTOs = modelMapper.map(expectedCourses, listType);

        Mockito.when(courseRepository.findAllByTeachersCount(eq(4))).thenReturn(expectedCourses);

        List<CourseDTO> actualCourses = courseService.getCoursesWithNumberOfAssignedTeachers(4);

        Assertions.assertThat(actualCourses)
                .as("Wrong courses have been returned by Course Service after using " +
                        "'getCoursesWithNumberOfAssignedTeachers' method")
                .isEqualTo(expectedCourseDTOs);
    }

    @Test
    public void getFilteredCourses_RetrieveFinishedCourses_ReturnCourses() {
        List<Course> courses = JsonParser.buildObjectFromJSON(CourseJsonData.COURSES_FOR_FIND_ALL_BY_END_DATE_LESS_THAN,
                new TypeReference<>() {});
        LocalDate comparisonDate = LocalDate.now();
        List<Course> expectedCourses = courses.stream().filter(course -> course.getEndDate().isBefore(comparisonDate))
                .collect(Collectors.toList());

        Mockito.when(courseRepository.findAllByEndDateLessThan(eq(comparisonDate))).thenReturn(expectedCourses);

        List<CourseDTO> actualCourses = courseService.getFilteredCourses(CourseCriteria.FINISHED);

        Assertions.assertThat(actualCourses)
                .usingFieldByFieldElementComparator()
                .as("Wrong courses have been returned by Course Service after using " +
                        "'getFilteredCourses' method with CourseCriteria.FINISHED parameter")
                .isEqualTo(expectedCourses);
    }

    @Test
    public void getCoursesThatLast_RetrieveCoursesThatLastSpecificAmountOfTime_ReturnCourses() {
        List<Course> courses = JsonParser.buildObjectFromJSON(CourseJsonData.COURSES_FOR_FIND_ALL_THAT_LAST,
                new TypeReference<>() {});
        List<Course> expectedCourses = courses.stream().filter(course -> DAYS.between(course.getStartDate(),
                course.getEndDate()) == 3).collect(Collectors.toList());

        Mockito.when(courseRepository.findAllByDateDiffBetweenStartDateAndEndDateEqualTo(eq(3)))
                .thenReturn(expectedCourses);

        List<CourseDTO> actualCourses = courseService.getCoursesThatLast(3);

        Assertions.assertThat(actualCourses)
                .usingFieldByFieldElementComparator()
                .as("Actual courses returned by Course Service after using 'getCoursesThatLast' method " +
                        "differ from the expected ones")
                .isEqualTo(expectedCourses);
    }

    @Test
    public void deleteCourse_RemoveSpecifiedCourse() {
        courseService.deleteCourse(4);

        Mockito.verify(courseRepository, Mockito.times(1)).deleteById(Mockito.eq(4));
    }

    // Negative scenarios:

    @Test
    public void createCourse_CreateCourseWithNullName_ThrowException() {
        Mockito.when(courseRepository.save(eq(new Course(null)))).thenThrow(new IllegalCourseArgumentException());

        Throwable thrownException = catchThrowable(() -> courseService.createCourse(null));

        Assertions.assertThat(thrownException)
                .as("Expected exception has not been thrown on an attempt to create a course with null name")
                .isInstanceOf(IllegalCourseArgumentException.class);
        Assertions.assertThat(thrownException.getMessage())
                .as("Actual exception message differs from the expected one")
                .isEqualTo("IIllegal argument for Course object has been passed");
    }

    @Test
    public void createCourse_CreateCourseWithEmptyName_TriggerValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setName("");
        Set<ConstraintViolation<CourseDTO>> violations = validator.validate(courseDTO);

        Assertions.assertThat(violations.size())
                .as("Validation of an empty course name has not been initiated")
                .isEqualTo(1);
        Assertions.assertThat(violations.iterator().next().getMessage())
                .as("Actual validation message for empty course name differs from the expected one")
                .isEqualTo("Course cannot be registered without a name");
    }
}
