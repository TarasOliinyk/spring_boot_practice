package com.springboot.practice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.practice.dto.TeacherDTO;
import com.springboot.practice.unit.service.CourseService;
import com.springboot.practice.unit.service.TeacherService;
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

        String response = this.mockMvc.perform(MockMvcRequestBuilders.post("/teacher")
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
    public void findOne_RetrieveTeacher_ReturnTeacher() throws Exception {
        TeacherDTO expectedTeacherDTO = JsonParser.buildObjectFromJSON(TeacherJsonData.TEACHER_FOR_FIND_ONE_POSITIVE,
                new TypeReference<>() {});

        Mockito.when(teacherService.getTeacher(eq(expectedTeacherDTO.getId()))).thenReturn(expectedTeacherDTO);

        String response = this.mockMvc.perform(MockMvcRequestBuilders.get("/teacher/" + expectedTeacherDTO.getId()))
                .andDo(print()).andExpect(status().isFound()).andReturn().getResponse().getContentAsString();

        TeacherDTO actualTeacherDTO = objectMapper.readValue(response, TeacherDTO.class);

        Assertions.assertThat(actualTeacherDTO)
                .as("Actual teacher returned by Teacher Controller using 'getTeacher' method differs from " +
                        "the expected one")
                .isEqualTo(expectedTeacherDTO);
    }
}