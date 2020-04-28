package com.springboot.practice.integration.sevice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.springboot.practice.dto.CourseDTO;
import com.springboot.practice.model.Course;
import com.springboot.practice.repository.CourseRepository;
import com.springboot.practice.unit.service.CourseService;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CourseServiceTest {
    @Autowired
    private CourseService courseService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @MockBean
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
}