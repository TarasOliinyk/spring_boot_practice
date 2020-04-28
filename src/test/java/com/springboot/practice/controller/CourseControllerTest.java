package com.springboot.practice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.practice.dto.CourseDTO;
import com.springboot.practice.unit.service.CourseService;
import com.springboot.practice.unit.service.TeacherService;
import com.springboot.practice.utils.json.JsonParser;
import com.springboot.practice.utils.json.supplier.course.CourseJsonData;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CourseController.class)
public class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CourseService courseService;
    @MockBean
    private TeacherService teacherService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createCourse_CreateNewCourse_ReturnCreatedCourse() throws Exception {
        CourseDTO expectedCourseDTO = JsonParser.buildObjectFromJSON(CourseJsonData.COURSE_FOR_CREATE_ONE_POSITIVE,
                new TypeReference<>() {});
        String expectedCourseDTOAsJSON = JsonParser.readJSON(CourseJsonData.COURSE_FOR_CREATE_ONE_POSITIVE);

        Mockito.when(courseService.createCourse(eq(expectedCourseDTO.getName()))).thenReturn(expectedCourseDTO);

        String response = this.mockMvc.perform(MockMvcRequestBuilders.post("/course")
                .contentType(MediaType.APPLICATION_JSON)
                .content(expectedCourseDTOAsJSON)).andDo(print()).andExpect(status().isCreated()).andReturn()
                .getResponse().getContentAsString();

        CourseDTO actualCourseDTO = objectMapper.readValue(response, CourseDTO.class);

        Assertions.assertThat(actualCourseDTO)
                .as("Actual course created by Course Controller using 'createCourse' method differs from " +
                        "the expected one")
                .isEqualTo(expectedCourseDTO);
    }

    @Test
    public void createCourseWithStartAndEndDates_CreateNewCourse_ReturnCreatedCourse() throws Exception {
        CourseDTO expectedCourseDTO = JsonParser.buildObjectFromJSON(CourseJsonData.COURSE_FOR_CREATE_ONE_WITH_DATES_POSITIVE,
                new TypeReference<>() {});
        String expectedCourseDTOAsJSON = JsonParser.readJSON(CourseJsonData.COURSE_FOR_CREATE_ONE_WITH_DATES_POSITIVE);

        Mockito.when(courseService.createCourseWithStartAndEndDates(eq(expectedCourseDTO.getName()),
                eq(expectedCourseDTO.getStartDate()), eq(expectedCourseDTO.getEndDate()))).thenReturn(expectedCourseDTO);

        String response = this.mockMvc.perform(MockMvcRequestBuilders.post("/course_with_dates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(expectedCourseDTOAsJSON)).andDo(print()).andExpect(status().isCreated()).andReturn()
                .getResponse().getContentAsString();

        CourseDTO actualCourseDTO = objectMapper.readValue(response, CourseDTO.class);

        Assertions.assertThat(actualCourseDTO)
                .as("Actual course created by Course Controller using 'createCourseWithStartAndEndDates' " +
                        "method differs from the expected one")
                .isEqualTo(expectedCourseDTO);
    }

    @Test
    public void findOne_RetrieveCourse_ReturnCourse() throws Exception {
        CourseDTO expectedCourseDTO = JsonParser.buildObjectFromJSON(CourseJsonData.COURSE_FOR_FIND_ONE_POSITIVE,
                new TypeReference<>() {});

        Mockito.when(courseService.getCourse(eq(expectedCourseDTO.getId()))).thenReturn(expectedCourseDTO);

        String response = this.mockMvc.perform(MockMvcRequestBuilders.get("/course/" + expectedCourseDTO.getId()))
                .andDo(print()).andExpect(status().isFound()).andReturn().getResponse().getContentAsString();

        CourseDTO actualCourseDTO = objectMapper.readValue(response, CourseDTO.class);

        Assertions.assertThat(actualCourseDTO)
                .as("Actual course returned by Course Controller using 'getCourse' method differs from the " +
                        "@expected one")
                .isEqualTo(expectedCourseDTO);
    }
}