package com.springboot.practice.integration.sevice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.springboot.practice.dto.CourseDTO;
import com.springboot.practice.dto.TeacherDTO;
import com.springboot.practice.model.Course;
import com.springboot.practice.model.Teacher;
import com.springboot.practice.repository.CourseRepository;
import com.springboot.practice.unit.service.CourseService;
import com.springboot.practice.unit.service.criteria.CourseCriteria;
import com.springboot.practice.utils.json.JsonParser;
import com.springboot.practice.utils.json.supplier.course.CourseJsonData;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.eq;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CourseServiceTest {
    @Autowired
    private CourseService courseService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @MockBean // ToDo: Mocked for testing as a POC
    private CourseRepository courseRepository;

    @Test
    public void createCourse_CreateNewCourse_ReturnCreatedCourse() {
        Course course = JsonParser.buildObjectFromJSON(CourseJsonData.COURSE_FOR_CREATE_ONE_POSITIVE,
                new TypeReference<>() {});
        CourseDTO expectedCourseDTO = JsonParser.buildObjectFromJSON(CourseJsonData.COURSE_FOR_CREATE_ONE_POSITIVE,
                new TypeReference<>() {});

        Mockito.when(courseRepository.save(eq(course))).thenReturn(course);

        CourseDTO actualCourseDTO = courseService.createCourse(course.getName());

        Assertions.assertThat(actualCourseDTO)
                .as("Actual course returned by Course Service using 'createCourse' method differs from the " +
                        "expected one")
                .isEqualTo(expectedCourseDTO);
    }

    @Test
    public void findOne_RetrieveCourse_ReturnCourse() {
        Course course = JsonParser.buildObjectFromJSON(CourseJsonData.COURSE_FOR_FIND_ONE_POSITIVE,
                new TypeReference<>() {});
        CourseDTO expectedCourseDTO = JsonParser.buildObjectFromJSON(CourseJsonData.COURSE_FOR_FIND_ONE_POSITIVE,
                new TypeReference<>() {});

        Mockito.when(courseRepository.findOneById(eq(course.getId()))).thenReturn(Optional.of(course));

        CourseDTO actualCourseDTO = courseService.getCourse(course.getId());

        Assertions.assertThat(actualCourseDTO)
                .as("Actual course returned by Course Service using 'getCourse' method differs from the " +
                        "expected one")
                .isEqualTo(expectedCourseDTO);
    }

    @Test
    public void findAll_RetrieveAllCourses_ReturnCourses() {
        List<Course> expectedCourses = JsonParser.buildObjectFromJSON(CourseJsonData.COURSES_FOR_FIND_ALL_POSITIVE,
                new TypeReference<>() {});

        Mockito.when(courseRepository.findAll()).thenReturn(expectedCourses);

        List<CourseDTO> actualCourseDTOs = courseService.getAllCourses();

        Assertions.assertThat(actualCourseDTOs)
                .usingFieldByFieldElementComparator()
                .as("Actual courses returned by Course Service using 'getAllCourses' method differ from the " +
                        "expected ones")
                .isEqualTo(expectedCourses);
    }

    @Test
    public void findAllByTeachersContaining_RetrieveCoursesThatContainSpecificTeacher_ReturnCourses() {
        List<Course> expectedCourses = JsonParser.buildObjectFromJSON(CourseJsonData.COURSES_FOR_FIND_ALL_BY_TEACHERS_CONTAINING,
                new TypeReference<>() {});
        Teacher teacher = JsonParser.buildObjectFromJSON(CourseJsonData.TEACHER_FOR_FIND_ALL_BY_TEACHERS_CONTAINING,
                new TypeReference<>() {});
        TeacherDTO teacherDTO = JsonParser.buildObjectFromJSON(CourseJsonData.TEACHER_FOR_FIND_ALL_BY_TEACHERS_CONTAINING,
                new TypeReference<>() {});

        Mockito.when(courseRepository.findAllByTeachersContaining(eq(teacher))).thenReturn(expectedCourses);

        List<CourseDTO> actualCourseDTOs = courseService.getAllCoursesAssignedToTeacher(teacherDTO);

        Assertions.assertThat(actualCourseDTOs)
                .usingFieldByFieldElementComparator()
                .as("Actual courses returned by Course Service using 'getAllCoursesAssignedToTeacher' method " +
                        "differ from the expected ones")
                .isEqualTo(expectedCourses);
    }

    @Test
    public void findAllByStartDateGreaterThan_RetrieveAllNotStartedCourses_ReturnCourses() {
        List<Course> courses = JsonParser.buildObjectFromJSON(
                CourseJsonData.COURSES_FOR_FIND_ALL_BY_START_DATE_GREATER_THAN, new TypeReference<>() {});
        LocalDate comparisonDate = LocalDate.now();
        List<Course> expectedCourses = courses.stream().filter(course -> course.getStartDate().isAfter(comparisonDate))
                .collect(Collectors.toList());

        Mockito.when(courseRepository.findAllByStartDateGreaterThan(eq(comparisonDate))).thenReturn(expectedCourses);

        List<CourseDTO> actualCourseDTOs = courseService.getFilteredCourses(CourseCriteria.NOT_STARTED);

        Assertions.assertThat(actualCourseDTOs)
                .usingFieldByFieldElementComparator()
                .as("Actual courses returned by Course Service using 'getFilteredCourses' method with " +
                        "'not_started' parameter differ from the expected ones")
                .isEqualTo(expectedCourses);
    }

    @Test
    public void findAllByEndDateLessThan_RetrieveAllFinishedCourses_ReturnCourses() {
        List<Course> courses = JsonParser.buildObjectFromJSON(CourseJsonData.COURSES_FOR_FIND_ALL_BY_END_DATE_LESS_THAN,
                new TypeReference<>() {});
        LocalDate comparisonDate = LocalDate.now();
        List<Course> expectedCourses = courses.stream().filter(course -> course.getEndDate().isBefore(comparisonDate))
                .collect(Collectors.toList());

        Mockito.when(courseRepository.findAllByEndDateLessThan(eq(comparisonDate))).thenReturn(expectedCourses);

        List<CourseDTO> actualCourseDTOs = courseService.getFilteredCourses(CourseCriteria.FINISHED);

        Assertions.assertThat(actualCourseDTOs)
                .usingFieldByFieldElementComparator()
                .as("Actual courses returned by Course Service using 'getFilteredCourses' method with " +
                        "'finished' parameter differ from the expected ones")
                .isEqualTo(expectedCourses);
    }

    @Test
    public void findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual_RetrieveAllOngoingCourses_ReturnCourses() {
        List<Course> courses = JsonParser.buildObjectFromJSON(
                CourseJsonData.COURSES_FOR_FIND_ALL_BY_START_DATE_LESS_THAN_EQUAL_AND_END_DATE_GREATER_THAN_EQUAL,
                new TypeReference<>() {});
        LocalDate comparisonDate = LocalDate.now();
        List<Course> expectedCourses = courses.stream().filter(course -> course.getStartDate()
                .isBefore(comparisonDate) && course.getEndDate().isAfter(comparisonDate)).collect(Collectors.toList());

        Mockito.when(courseRepository.findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(eq(comparisonDate),
                eq(comparisonDate))).thenReturn(expectedCourses);

        List<CourseDTO> actualCourses = courseService.getFilteredCourses(CourseCriteria.ONGOING);

        Assertions.assertThat(actualCourses)
                .usingFieldByFieldElementComparator()
                .as("Actual courses returned by Course Service using 'getFilteredCourses' method with " +
                        "'ongoing' parameter differ from the expected ones")
                .isEqualTo(expectedCourses);
    }

    @Test
    public void findAllByDateDiffBetweenStartDateAndEndDateEqualTo_RetrieveCoursesThatLastSpecificNumberOfDays_ReturnCourses() {
        int expectedNumberOfDays = 3;
        List<Course> courses = JsonParser.buildObjectFromJSON(CourseJsonData.COURSES_FOR_FIND_ALL_THAT_LAST,
                new TypeReference<>() {});
        List<Course> expectedCourses = new ArrayList<>();

        Mockito.when(courseRepository.findAllByDateDiffBetweenStartDateAndEndDateEqualTo(eq(expectedNumberOfDays)))
                .thenAnswer(invocationOnMock -> {
                    expectedCourses.addAll(courses.stream().filter(course -> ChronoUnit.DAYS.between(course.getStartDate(),
                            course.getEndDate()) == expectedNumberOfDays).collect(Collectors.toList()));
                    return expectedCourses;
                });

        List<CourseDTO> actualCourseDTOs = courseService.getCoursesThatLast(expectedNumberOfDays);

        Assertions.assertThat(actualCourseDTOs)
                .as("Actual number of courses returned by Course Service using 'getCoursesThatLast' method " +
                        "differs from the expected one")
                .hasSize(2)
                .usingFieldByFieldElementComparator()
                .as("Actual courses returned by Course Service using 'getCoursesThatLast' method differ " +
                        "from the expected ones")
                .isEqualTo(expectedCourses);
    }
}