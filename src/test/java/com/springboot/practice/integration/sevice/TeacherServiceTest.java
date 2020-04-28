package com.springboot.practice.integration.sevice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.springboot.practice.dto.TeacherDTO;
import com.springboot.practice.model.Teacher;
import com.springboot.practice.repository.TeacherRepository;
import com.springboot.practice.unit.service.TeacherService;
import com.springboot.practice.utils.json.JsonParser;
import com.springboot.practice.utils.json.supplier.teacher.TeacherJsonData;
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
public class TeacherServiceTest {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @MockBean
    private TeacherRepository teacherRepository;

    @Test
    public void createTeacher_CreateNewTeacher_ReturnCreatedTeacher() {
        Teacher teacher = JsonParser.buildObjectFromJSON(TeacherJsonData.TEACHER_FOR_CREATE_ONE_POSITIVE,
                new TypeReference<>() {});
        TeacherDTO expectedTeacherDTO = JsonParser.buildObjectFromJSON(TeacherJsonData.TEACHER_FOR_CREATE_ONE_POSITIVE,
                new TypeReference<>() {});

        Mockito.when(teacherRepository.save(eq(teacher))).thenReturn(teacher);

        TeacherDTO actualTeacherDTO = teacherService.createTeacher(teacher.getFirstName(), teacher.getLastName(),
                teacher.getAge());

        Assertions.assertThat(actualTeacherDTO)
                .as("Actual teacher returned by Teacher Service using 'createTeacher' method differs from " +
                        "the expected one")
                .isEqualTo(expectedTeacherDTO);
    }

    @Test
    public void findOne_RetrieveTeacher_ReturnTeacher() {
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
}
