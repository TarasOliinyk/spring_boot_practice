package com.springboot.practice.service;

import com.springboot.practice.dto.CourseDTO;
import com.springboot.practice.dto.TeacherDTO;
import com.springboot.practice.model.Course;
import com.springboot.practice.model.Teacher;
import com.springboot.practice.repository.CourseRepository;
import com.springboot.practice.service.criteria.CourseCriteria;
import com.springboot.practice.service.implementation.CourseServiceImpl;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class CourseServiceImplTest {
    private CourseService courseService;
    @Mock
    private CourseRepository courseRepository;
    private ModelMapper modelMapper = new ModelMapper();
    private LocalDate today = LocalDate.now();

    @Before
    public void init() {
        courseService = new CourseServiceImpl(courseRepository, modelMapper);
    }

    @Test
    public void createCourse_CreateNewCourse_ReturnCourseDTO() {
        CourseDTO expectedCourseDTO = new CourseDTO();
        expectedCourseDTO.setName("MyCourse");

        Mockito.when(courseRepository.save(new Course("MyCourse"))).thenReturn(new Course("MyCourse"));

        CourseDTO actualCourseDTO = courseService.createCourse("MyCourse");

        Assertions.assertThat(actualCourseDTO.getName())
                .as("Actual name of a course created by the Course Service differs from the expected one")
                .isEqualTo(expectedCourseDTO.getName());
    }

    @Test
    public void createCourseWithStartAndEndDates_CreateNewCourseWithStartAndEndDates_ReturnCreatedCourse() {
        LocalDate threeDaysAfterToday = today.plusDays(3);

        CourseDTO expectedCourseDTO = new CourseDTO();
        expectedCourseDTO.setName("MyCourseWithDates");
        expectedCourseDTO.setStartDate(today);
        expectedCourseDTO.setEndDate(threeDaysAfterToday);

        Mockito.when(courseRepository.save(new Course("MyCourseWithDates", today, threeDaysAfterToday)))
                .thenReturn(new Course("MyCourseWithDates", today, threeDaysAfterToday));

        CourseDTO actualCourseDTO = courseService.createCourseWithStartAndEndDates("MyCourseWithDates", today,
                threeDaysAfterToday);

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
    public void updateCourse_UpdateSpecificCourse_ReturnUpdateCourse() {
        LocalDate fiveDaysAfterToday = today.plusDays(5);
        LocalDate tenDaysAfterToday = today.plusDays(10);

        CourseDTO expectedCourseDTO = new CourseDTO();
        expectedCourseDTO.setId(23);
        expectedCourseDTO.setName("MyCourseUpdated");
        expectedCourseDTO.setStartDate(fiveDaysAfterToday);
        expectedCourseDTO.setEndDate(tenDaysAfterToday);

        Course course = new Course("MyCourseUpdated", fiveDaysAfterToday, tenDaysAfterToday);
        course.setId(23);

        Mockito.when(courseRepository.save(course)).thenReturn(course);

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(23);
        courseDTO.setName("MyCourseUpdated");
        courseDTO.setStartDate(fiveDaysAfterToday);
        courseDTO.setEndDate(tenDaysAfterToday);

        CourseDTO actualCourseDTO = courseService.updateCourse(courseDTO);

        Assertions.assertThat(actualCourseDTO)
                .as("Course updated by the Course Service has not been updated as expected")
                .isEqualToComparingFieldByField(expectedCourseDTO);
    }

    @Test
    public void getCourse_RetrieveCourse_ReturnCourse() {
        Course course = new Course("TestName", today, today.plusDays(5));
        course.setId(34);

        CourseDTO expectedCourseDTO = new CourseDTO();
        expectedCourseDTO.setId(34);
        expectedCourseDTO.setName("TestName");
        expectedCourseDTO.setStartDate(today);
        expectedCourseDTO.setEndDate(today.plusDays(5));

        Mockito.when(courseRepository.findOneById(34)).thenReturn(Optional.of(course));

        CourseDTO actualCourseDTO = courseService.getCourse(34);

        Assertions.assertThat(actualCourseDTO)
                .as("Actual course retrieved by Course Service differs from the expected one")
                .isEqualTo(expectedCourseDTO);
    }

    @Test
    public void getAllCourses_RetrieveAllCourses_ReturnCourses() {
        CourseDTO courseDTOA = new CourseDTO();
        CourseDTO courseDTOB = new CourseDTO();
        courseDTOA.setName("CourseA");
        courseDTOB.setName("CourseB");
        List<CourseDTO> expectedDTOs = Arrays.asList(courseDTOA, courseDTOB);

        Mockito.when(courseRepository.findAll()).thenReturn(Arrays.asList(new Course("CourseA"), new Course("CourseB")));

        List<CourseDTO> actualDTOs = courseService.getAllCourses();

        Assertions.assertThat(actualDTOs)
                .as("Courses retrieved by Course Service using 'getAllCourses' method differ from the expected courses")
                .isEqualTo(expectedDTOs);
    }

    @Test
    public void getAllCoursesAssignedToTeacher_RetrieveCoursesOfTeacher_ReturnListOfCourses() {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(4);
        Teacher teacher = new Teacher();
        teacher.setId(4);
        List<Course> expectedCourses = Arrays.asList(new Course("Course1"), new Course("Course2"));

        Mockito.when(courseRepository.findAllByTeachersContaining(teacher)).thenReturn(expectedCourses);

        List<CourseDTO> actualCourses = courseService.getAllCoursesAssignedToTeacher(teacherDTO);
        List<String> actualCourseNames = actualCourses.stream().map(CourseDTO::getName).collect(Collectors.toList());
        List<String> expectedCourseNames = expectedCourses.stream().map(Course::getName).collect(Collectors.toList());

        Assertions.assertThat(actualCourseNames)
                .as("Actual courses retrieved by Course Service using 'getAllCoursesAssignedToTeacher' " +
                        "method differ from the expected ones")
                .isEqualTo(expectedCourseNames);
    }

    @Test
    public void assignTeacherToCourse_SetTeachersToCourse_ReturnCourse() {
        Teacher teacher = new Teacher("Mike", "Tyson", 34);
        teacher.setId(6);

        Course course = new Course();
        course.setId(5);
        course.getTeachers().add(teacher);

        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(6);
        teacherDTO.setFirstName("Mike");
        teacherDTO.setLastName("Tyson");
        teacherDTO.setAge(34);

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(5);

        Mockito.when(courseRepository.save(course)).thenReturn(course);

        CourseDTO actualCourseDTO = courseService.assignTeacherToCourse(courseDTO, teacherDTO);

        Assertions.assertThat(actualCourseDTO.getTeachers().size())
                .as("Course has not been update by Course Service using 'assignTeacherToCourse' method")
                .isEqualTo(1);
        Assertions.assertThat(actualCourseDTO.getTeachers().get(0))
                .as("Expected teacher has not been assigned to course by Course Service using " +
                        "'assignTeacherToCourse' method")
                .isEqualTo(teacher);
    }

    @Test
    public void unassignTeacherFromCourse_RemoveTeacherFromCourse_ReturnCourse() {
        Teacher teacherA = new Teacher("John", "Travolta", 54);
        teacherA.setId(4);
        Teacher teacherB = new Teacher("Nick", "Cage", 43);
        teacherB.setId(5);
        List<Teacher> courseTeachers = Arrays.asList(teacherA, teacherB);

        Course course = new Course();
        course.setId(3);
        course.getTeachers().add(teacherB);

        TeacherDTO firstTeacherDTO = new TeacherDTO();
        firstTeacherDTO.setFirstName("John");
        firstTeacherDTO.setLastName("Travolta");
        firstTeacherDTO.setAge(54);

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(3);
        courseDTO.getTeachers().addAll(courseTeachers);

        Mockito.when(courseRepository.save(course)).thenReturn(course);

        CourseDTO actualCourseDTO = courseService.unassignTeacherFromCourse(courseDTO, firstTeacherDTO);

        Assertions.assertThat(actualCourseDTO.getTeachers().size())
                .as("Course has not been update by Course Service using 'unassignTeacherFromCourse' method")
                .isEqualTo(courseTeachers.size() - 1);
        Assertions.assertThat(actualCourseDTO.getTeachers().get(0))
                .as("Wrong teacher has been unassigned from course by Course Service using " +
                        "'unassignTeacherFromCourse' method")
                .isEqualTo(teacherB);
    }

    @Test
    public void getCoursesWithNumberOfAssignedTeachers_RetrieveCoursesWithSpecificNumberOfTeachers_ReturnCourses() {
        Course courseA = new Course();
        courseA.setId(1);
        Course courseB = new Course();
        courseB.setId(2);
        Course courseC = new Course();
        courseC.setId(3);

        courseA.getTeachers().addAll(Arrays.asList(new Teacher(), new Teacher(), new Teacher(), new Teacher()));
        courseB.getTeachers().addAll(Arrays.asList(new Teacher(), new Teacher(), new Teacher()));
        courseC.getTeachers().addAll(Arrays.asList(new Teacher(), new Teacher(), new Teacher(), new Teacher()));

        Mockito.when(courseRepository.findAllByTeachersCount(4)).thenReturn(Arrays.asList(courseA, courseC));

        List<CourseDTO> actualListOfCourses = courseService.getCoursesWithNumberOfAssignedTeachers(4);
        List<Integer> idsOfFilteredCourses = actualListOfCourses.stream().map(CourseDTO::getId).collect(Collectors.toList());
        List<Integer> idsOfExpectedCourses = Arrays.asList(courseA.getId(), courseC.getId());

        Assertions.assertThat(idsOfFilteredCourses)
                .as("Wrong courses have been returned by Course Service after using " +
                        "'getCoursesWithNumberOfAssignedTeachers' method")
                .isEqualTo(idsOfExpectedCourses);
    }

    @Test
    public void getFilteredCourses_RetrieveFinishedCourses_ReturnCourses() {
        Course courseA = new Course();
        courseA.setId(7);
        Course courseB = new Course();
        courseB.setId(8);
        Course courseC = new Course();
        courseC.setId(9);

        courseA.setStartDate(today);
        courseA.setEndDate(today.plusDays(5));
        courseB.setStartDate(today.minusDays(10));
        courseB.setEndDate(today.minusDays(2));
        courseC.setStartDate(today.minusDays(4));
        courseC.setEndDate(today.minusDays(1));

        Mockito.when(courseRepository.findAllByEndDateLessThan(LocalDate.now())).thenReturn(Arrays.asList(courseB, courseC));

        List<CourseDTO> actualCourses = courseService.getFilteredCourses(CourseCriteria.FINISHED);
        List<Integer> idsOfFilteredCourses = actualCourses.stream().map(CourseDTO::getId).collect(Collectors.toList());
        List<Integer> idsOfExpectedCourses = Arrays.asList(courseB.getId(), courseC.getId());

        Assertions.assertThat(idsOfFilteredCourses)
                .as("Wrong courses have been returned by Course Service after using " +
                        "'getFilteredCourses' method with CourseCriteria.FINISHED parameter")
                .isEqualTo(idsOfExpectedCourses);
    }

    @Test
    public void getCoursesThatLast_RetrieveCoursesThatLastSpecificAmountOfTime_ReturnCourses() {
        Course courseA = new Course();
        courseA.setId(9);
        Course courseB = new Course();
        courseB.setId(10);
        List<Course> expectedCourses = Arrays.asList(courseA, courseB);

        Mockito.when(courseRepository.findAllByDateDiffBetweenStartDateAndEndDateEqualTo(3)).thenReturn(expectedCourses);

        List<CourseDTO> actualCourses = courseService.getCoursesThatLast(3);
        List<Integer> idsOfFilteredCourses = actualCourses.stream().map(CourseDTO::getId).collect(Collectors.toList());
        List<Integer> idsOfExpectedCourses = expectedCourses.stream().map(Course::getId).collect(Collectors.toList());

        Assertions.assertThat(idsOfFilteredCourses)
                .as("Actual courses returned by Course Service after using 'getCoursesThatLast' method " +
                        "differ from the expected ones")
                .isEqualTo(idsOfExpectedCourses);
    }

    @Test
    public void deleteCourse_RemoveSpecifiedCourse() {
        courseService.deleteCourse(4);

        Mockito.verify(courseRepository, times(1)).deleteById(Mockito.eq(4));
    }
}
