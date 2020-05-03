package com.springboot.practice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.practice.dto.CourseDTO;
import com.springboot.practice.dto.TeacherDTO;
import com.springboot.practice.model.Teacher;
import com.springboot.practice.unit.service.CourseService;
import com.springboot.practice.unit.service.TeacherService;
import com.springboot.practice.unit.service.criteria.CourseCriteria;
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

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/course")
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

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/course_with_dates")
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

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/course/" + expectedCourseDTO.getId()))
                .andDo(print()).andExpect(status().isFound()).andReturn().getResponse().getContentAsString();

        CourseDTO actualCourseDTO = objectMapper.readValue(response, CourseDTO.class);

        Assertions.assertThat(actualCourseDTO)
                .as("Actual course returned by Course Controller using 'getCourse' method differs from the " +
                        "@expected one")
                .isEqualTo(expectedCourseDTO);
    }

    @Test
    public void getAllCourses_RetrieveAllCourses_ReturnCourses() throws Exception {
        List<CourseDTO> expectedCourseDTOs = JsonParser.buildObjectFromJSON(CourseJsonData.COURSES_FOR_FIND_ALL_POSITIVE,
                new TypeReference<>() {});

        Mockito.when(courseService.getAllCourses()).thenReturn(expectedCourseDTOs);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/course/list")).andDo(print())
                .andExpect(status().isFound()).andReturn().getResponse().getContentAsString();

        List<CourseDTO> actualCourseDTOs = objectMapper.readValue(response, new TypeReference<>() {});

        Assertions.assertThat(actualCourseDTOs)
                .as("Actual courses returned by Course Controller using 'getAllCourses' method differ " +
                        "from the expected ones")
                .isEqualTo(expectedCourseDTOs);
    }

    @Test
    public void getAllCoursesAssignedToTeacher_RetrieveAllCoursesOfTeacher_ReturnCourses() throws Exception {
        TeacherDTO teacherDTO = JsonParser.buildObjectFromJSON(CourseJsonData.TEACHER_FOR_FIND_ALL_BY_TEACHERS_CONTAINING,
                new TypeReference<>() {});
        List<CourseDTO> expectedCourseDTOs = JsonParser.buildObjectFromJSON(
                CourseJsonData.COURSES_FOR_FIND_ALL_BY_TEACHERS_CONTAINING, new TypeReference<>() {});

        Mockito.when(teacherService.getTeacher(eq(teacherDTO.getId()))).thenReturn(teacherDTO);
        Mockito.when(courseService.getAllCoursesAssignedToTeacher(eq(teacherDTO))).thenReturn(expectedCourseDTOs);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/courses/teacher/" + teacherDTO.getId()))
                .andDo(print()).andExpect(status().isFound()).andReturn().getResponse().getContentAsString();

        List<CourseDTO> actualCourseDTOs = objectMapper.readValue(response, new TypeReference<>() {});

        Assertions.assertThat(actualCourseDTOs)
                .as("Actual courses returned by Course Controller using 'getAllCoursesAssignedToTeacher' " +
                        "method differ from the expected ones")
                .isEqualTo(expectedCourseDTOs);
    }

    @Test
    public void assignTeacher_AssignTeacherToCourse_ReturnCourse() throws Exception {
        TeacherDTO teacherDTO = JsonParser.buildObjectFromJSON(CourseJsonData.TEACHER_FOR_ASSIGN_TEACHER_TO_COURSE,
                new TypeReference<>() {});
        Teacher teacher = JsonParser.buildObjectFromJSON(CourseJsonData.TEACHER_FOR_ASSIGN_TEACHER_TO_COURSE,
                new TypeReference<>() {});
        CourseDTO courseDTO = JsonParser.buildObjectFromJSON(CourseJsonData.COURSE_FOR_ASSIGN_TEACHER_TO_COURSE,
                new TypeReference<>() {});
        courseDTO.getTeachers().add(teacher);

        Mockito.when(courseService.getCourse(eq(courseDTO.getId()))).thenReturn(courseDTO);
        Mockito.when(teacherService.getTeacher(eq(teacher.getId()))).thenReturn(teacherDTO);
        Mockito.when(courseService.assignTeacherToCourse(eq(courseDTO), eq(teacherDTO))).thenReturn(courseDTO);

        String response = mockMvc.perform(MockMvcRequestBuilders.put(String.format("/course/%s/add_teacher/%s",
                courseDTO.getId(), teacherDTO.getId()))).andDo(print()).andExpect(status().isOk()).andReturn()
                .getResponse().getContentAsString();

        CourseDTO actualCourseDTO = objectMapper.readValue(response, CourseDTO.class);

        Assertions.assertThat(actualCourseDTO.getTeachers().size())
                .as("Course has not been updated by Course Controller using 'assignTeacher' method")
                .isEqualTo(1);
        Assertions.assertThat(actualCourseDTO.getTeachers().get(0))
                .as("Expected teacher has not been assigned to course by Course Controller using " +
                        "'assignTeacher' method")
                .isEqualTo(teacher);
    }

    @Test
    public void unassignTeacher_RemoveTeacherFromCourse_ReturnCourse() throws Exception {
        List<Teacher> teachers = JsonParser.buildObjectFromJSON(CourseJsonData.TEACHERS_FOR_UNASSIGN_TEACHER_FROM_COURSE,
                new TypeReference<>() {});
        List<TeacherDTO> teacherDTOs = JsonParser.buildObjectFromJSON(CourseJsonData.TEACHERS_FOR_UNASSIGN_TEACHER_FROM_COURSE,
                new TypeReference<>() {});
        TeacherDTO teacherDTOToRemove = teacherDTOs.get(0);
        CourseDTO courseDTO = JsonParser.buildObjectFromJSON(CourseJsonData.COURSE_FOR_UNASSIGN_TEACHER_FROM_COURSE,
                new TypeReference<>() {});
        courseDTO.getTeachers().addAll(teachers);

        Mockito.when(courseService.getCourse(eq(courseDTO.getId()))).thenReturn(courseDTO);
        Mockito.when(teacherService.getTeacher(eq(teacherDTOToRemove.getId()))).thenReturn(teacherDTOToRemove);
        Mockito.when(courseService.unassignTeacherFromCourse(eq(courseDTO), eq(teacherDTOToRemove)))
                .thenAnswer(invocationOnMock -> {
                    CourseDTO courseToUpdate = invocationOnMock.getArgument(0);
                    courseToUpdate.getTeachers().remove(teachers.get(0));
                    return courseToUpdate;
                });

        String response = mockMvc.perform(MockMvcRequestBuilders.put(String.format("/course/%s/remove_teacher/%s",
                courseDTO.getId(), teacherDTOToRemove.getId()))).andDo(print()).andExpect(status().isOk()).andReturn()
                .getResponse().getContentAsString();

        CourseDTO actualCourseDTO = objectMapper.readValue(response, CourseDTO.class);

        Assertions.assertThat(actualCourseDTO.getTeachers().size())
                .as("Course has not been updated by Course Controller using 'unassignTeacher' method")
                .isEqualTo(teachers.size() - 1);
        Assertions.assertThat(actualCourseDTO.getTeachers().get(0))
                .as("Wrong teacher has been unassigned from the course by Course Controller using " +
                        "'unassignTeacher' method")
                .isEqualTo(teachers.get(1));
    }

    @Test
    public void getCoursesWithNumberOfAssignedTeachers_RetrieveCoursesWithNumberOfTeachers_ReturnCourses() throws Exception {
        int expectedNumberOfTeachers = 4;
        List<CourseDTO> courseDTOs = JsonParser.buildObjectFromJSON(CourseJsonData.COURSES_FOR_FIND_ALL_BY_TEACHERS_COUNT,
                new TypeReference<>() {});

        courseDTOs.get(0).getTeachers().addAll(Arrays.asList(new Teacher(), new Teacher(), new Teacher(), new Teacher()));
        courseDTOs.get(1).getTeachers().addAll(Arrays.asList(new Teacher(), new Teacher(), new Teacher()));
        courseDTOs.get(2).getTeachers().addAll(Arrays.asList(new Teacher(), new Teacher(), new Teacher(), new Teacher()));

        List<CourseDTO> expectedCourseDTOs = courseDTOs.stream().filter(course ->
                course.getTeachers().size() == expectedNumberOfTeachers).collect(Collectors.toList());

        Mockito.when(courseService.getCoursesWithNumberOfAssignedTeachers(eq(expectedNumberOfTeachers)))
                .thenReturn(expectedCourseDTOs);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/course/teachers/" + expectedNumberOfTeachers))
                .andDo(print()).andExpect(status().isFound()).andReturn().getResponse().getContentAsString();

        List<CourseDTO> actualCourseDTOs = objectMapper.readValue(response, new TypeReference<>() {});

        Assertions.assertThat(actualCourseDTOs)
                .as("Actual courses returned by Course Controller using 'getCoursesWithNumberOfAssignedTeachers' " +
                        "method differ from the expected ones")
                .isEqualTo(expectedCourseDTOs);
    }

    @Test
    public void getFilteredCourses_RetrieveFinishedCourses_ReturnCourses() throws Exception {
        List<CourseDTO> courseDTOs = JsonParser.buildObjectFromJSON(CourseJsonData.COURSES_FOR_FIND_ALL_BY_END_DATE_LESS_THAN,
                new TypeReference<>() {});
        LocalDate comparisonDate = LocalDate.now();
        List<CourseDTO> expectedCourseDTOs = courseDTOs.stream().filter(course -> course.getEndDate().isBefore(comparisonDate))
                .collect(Collectors.toList());

        Mockito.when(courseService.getFilteredCourses(eq(CourseCriteria.FINISHED))).thenReturn(expectedCourseDTOs);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/courses/filter/finished"))
                .andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<CourseDTO> actualCourseDTOs = objectMapper.readValue(response, new TypeReference<>() {});

        Assertions.assertThat(actualCourseDTOs)
                .as("Actual courses returned by Course Controller using 'getFilteredCourses' method with " +
                        "'finished' sorting parameter differ from the expected ones")
                .isEqualTo(expectedCourseDTOs);
    }

    @Test
    public void getCoursesThatLastSpecificNumberOfDays_RetrieveCoursesThatLastSpecificNumberOfDays_ReturnCourses() throws Exception {
        int expectedNumberOfDays = 3;
        List<CourseDTO> courseDTOs = JsonParser.buildObjectFromJSON(CourseJsonData.COURSES_FOR_FIND_ALL_THAT_LAST,
                new TypeReference<>() {});
        List<CourseDTO> expectedCourseDTOs = courseDTOs.stream().filter(courseDTO ->
                ChronoUnit.DAYS.between(courseDTO.getStartDate(), courseDTO.getEndDate()) == expectedNumberOfDays)
                .collect(Collectors.toList());

        Mockito.when(courseService.getCoursesThatLast(eq(expectedNumberOfDays))).thenReturn(expectedCourseDTOs);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/courses/duration/" + expectedNumberOfDays))
                .andDo(print()).andExpect(status().isFound()).andReturn().getResponse().getContentAsString();

        List<CourseDTO> actualCourseDTOs = objectMapper.readValue(response, new TypeReference<>() {});

        Assertions.assertThat(actualCourseDTOs)
                .as("Actual courses returned by Course Controller using 'getCoursesThatLastSpecificNumberOfDays' " +
                        "method differ from the expected ones")
                .isEqualTo(expectedCourseDTOs);
    }

    @Test
    public void deleteCourse_RemoveCourse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/course/4")).andDo(print())
                .andExpect(status().isAccepted());
    }
}