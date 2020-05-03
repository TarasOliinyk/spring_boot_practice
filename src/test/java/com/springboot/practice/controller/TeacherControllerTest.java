package com.springboot.practice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.practice.dto.CourseDTO;
import com.springboot.practice.dto.TeacherDTO;
import com.springboot.practice.unit.service.CourseService;
import com.springboot.practice.unit.service.TeacherService;
import com.springboot.practice.unit.service.criteria.TeacherSortingCriteria;
import com.springboot.practice.utils.json.JsonParser;
import com.springboot.practice.utils.json.supplier.teacher.TeacherJsonData;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TeacherController.class)
public class TeacherControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TeacherService teacherService;
    @MockBean
    private CourseService courseService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createTeacher_CreateNewTeacher_ReturnCreatedTeacher() throws Exception {
        TeacherDTO expectedTeacherDTO = JsonParser.buildObjectFromJSON(TeacherJsonData.TEACHER_FOR_CREATE_ONE_POSITIVE,
                new TypeReference<>() {});
        String expectedTeacherDTOAsJSON = JsonParser.readJSON(TeacherJsonData.TEACHER_FOR_CREATE_ONE_POSITIVE);

        Mockito.when(teacherService.createTeacher(eq(expectedTeacherDTO.getFirstName()), eq(expectedTeacherDTO.getLastName()),
                eq(expectedTeacherDTO.getAge()))).thenReturn(expectedTeacherDTO);

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/teacher")
                .contentType(MediaType.APPLICATION_JSON)
                .content(expectedTeacherDTOAsJSON)).andDo(print()).andExpect(status().isCreated()).andReturn()
                .getResponse().getContentAsString();

        TeacherDTO actualTeacherDTO = objectMapper.readValue(response, TeacherDTO.class);

        Assertions.assertThat(actualTeacherDTO)
                .as("Actual teacher created by Teacher Controller using 'createTeacher' method differs from " +
                        "the expected one")
                .isEqualTo(expectedTeacherDTO);
    }

    @Test
    public void getTeacher_RetrieveTeacher_ReturnTeacher() throws Exception {
        TeacherDTO expectedTeacherDTO = JsonParser.buildObjectFromJSON(TeacherJsonData.TEACHER_FOR_FIND_ONE_POSITIVE,
                new TypeReference<>() {});

        Mockito.when(teacherService.getTeacher(eq(expectedTeacherDTO.getId()))).thenReturn(expectedTeacherDTO);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/teacher/" + expectedTeacherDTO.getId()))
                .andDo(print()).andExpect(status().isFound()).andReturn().getResponse().getContentAsString();

        TeacherDTO actualTeacherDTO = objectMapper.readValue(response, TeacherDTO.class);

        Assertions.assertThat(actualTeacherDTO)
                .as("Actual teacher returned by Teacher Controller using 'getTeacher' method differs from " +
                        "the expected one")
                .isEqualTo(expectedTeacherDTO);
    }

    @Test
    public void getAllTeachers_RetrieveAllTeachers_ReturnTeachers() throws Exception {
        List<TeacherDTO> expectedTeacherDTOs = JsonParser.buildObjectFromJSON(TeacherJsonData.TEACHERS_FOR_FIND_ALL_POSITIVE,
                new TypeReference<>() {});

        Mockito.when(teacherService.getAllTeachers()).thenReturn(expectedTeacherDTOs);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/teacher/list")).andDo(print())
                .andExpect(status().isFound()).andReturn().getResponse().getContentAsString();

        List<TeacherDTO> actualTeacherDTOs = objectMapper.readValue(response, new TypeReference<>() {});

        Assertions.assertThat(actualTeacherDTOs)
                .as("Actual teachers returned by Teacher Controller using 'getAllTeachers' method differ " +
                        "from the expected ones")
                .isEqualTo(expectedTeacherDTOs);
    }

    @Test
    public void getAllTeachersSortedByAge_RetrieveTeachersSortedByAgeAscending_ReturnTeachers() throws Exception {
        List<TeacherDTO> expectedTeacherDTOs = JsonParser.buildObjectFromJSON(
                TeacherJsonData.TEACHERS_FOR_FIND_ALL_SORTED_BY_AGE_ASC, new TypeReference<>() {});

        Mockito.when(teacherService.getAllTeachersSortedBy(eq(TeacherSortingCriteria.ASCENDING_BY_AGE)))
                .thenReturn(expectedTeacherDTOs);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/teachers/sorted_by_age/asc"))
                .andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<TeacherDTO> actualTeacherDTOs = objectMapper.readValue(response, new TypeReference<>() {});

        Assertions.assertThat(actualTeacherDTOs)
                .as("Actual teachers returned by Teacher Controller using 'getAllTeachersSortedByAge' " +
                        "method differ from the expected ones")
                .isEqualTo(expectedTeacherDTOs);
    }

    @Test
    public void getAllTeachersAssignedToCourse_RetrieveTeachersAssignedToCourse_ReturnTeachers() throws Exception {
        CourseDTO courseDTO = JsonParser.buildObjectFromJSON(TeacherJsonData.COURSE_FOR_FIND_ALL_BY_COURSES_CONTAINING,
                new TypeReference<>() {});
        List<TeacherDTO> expectedTeacherDTOs = JsonParser.buildObjectFromJSON(
                TeacherJsonData.TEACHERS_FOR_FIND_ALL_BY_COURSES_CONTAINING, new TypeReference<>() {});

        Mockito.when(courseService.getCourse(eq(courseDTO.getId()))).thenReturn(courseDTO);
        Mockito.when(teacherService.getAllTeachersAssignedToCourse(eq(courseDTO))).thenReturn(expectedTeacherDTOs);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/teachers/course/" + courseDTO.getId()))
                .andDo(print()).andExpect(status().isFound()).andReturn().getResponse().getContentAsString();

        List<TeacherDTO> actualTeacherDTOs = objectMapper.readValue(response, new TypeReference<>() {});

        Assertions.assertThat(actualTeacherDTOs)
                .as("Actual teachers returned by Teacher Controller using 'getAllTeachersAssignedToCourse' " +
                        "method differ from the expected ones")
                .isEqualTo(expectedTeacherDTOs);
    }

    @Test
    public void deleteTeacher_RemoveTeacher() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/teacher/7")).andDo(print())
                .andExpect(status().isAccepted());
    }
}