package com.springboot.practice.ui;

import com.springboot.practice.dto.CourseDTO;
import com.springboot.practice.dto.TeacherDTO;
import com.springboot.practice.unit.service.CourseService;
import com.springboot.practice.unit.service.TeacherService;
import com.springboot.practice.unit.service.criteria.CourseCriteria;
import com.springboot.practice.unit.service.criteria.TeacherSortingCriteria;
import org.springframework.boot.CommandLineRunner;

import java.util.List;

//@Component
public class ConsoleReader implements CommandLineRunner {
    private final CourseService courseService;
    private final TeacherService teacherService;

    public ConsoleReader(CourseService courseService, TeacherService teacherService) {
        this.courseService = courseService;
        this.teacherService = teacherService;
    }

    @Override
    public void run(String... args) {
        processRequests();
    }


    private void processRequests() {
        // Testing:

//        Course math = courseService.createCourse("Math");
//        Course physics = courseService.createCourse("Physics");
//        Teacher teacher = teacherService.createTeacher("Bob", "White", 37);

//        CourseDTO math = courseService.getCourse(1);
//        CourseDTO physics = courseService.getCourse(2);
//        TeacherDTO teacherMike = teacherService.getTeacher(1);
//        TeacherDTO teacherTom = teacherService.getTeacher(2);
//        TeacherDTO teacherBob = teacherService.getTeacher(3);

//        courseService.assignTeacherToCourse(physics, teacherBob);
//        courseService.assignTeacherToCourse(math, teacherMike);
//        courseService.assignTeacherToCourse(physics, teacherTom);

//        courseService.unassignTeacherFromCourse(math, teacherBob);

//        LocalDate courseStartDate = LocalDate.now().minusDays(10);
//        physics.setStartDate(courseStartDate);
//        physics.setEndDate(courseStartDate.plusDays(5));
//        courseService.updateCourse(physics);
//        Course law = courseService.createCourseWithStartAndEndDates("Law", courseStartDate, courseStartDate.plusDays(10));

        System.out.println("\n=====================================================================\n");
        System.out.println("COURSES:");
        courseService.getAllCourses().forEach(c -> System.out.println(c.toString()));
        System.out.println("TEACHERS:");
        teacherService.getAllTeachers().forEach(t -> System.out.println(t.toString()));
        System.out.println("COURSES ASSIGNED TO Mike:");
        List<CourseDTO> coursesAssignedToMike = courseService.getAllCoursesAssignedToTeacher(1);
        coursesAssignedToMike.forEach(mc -> System.out.println(mc.toString()));
        System.out.println("COURSES ASSIGNED TO Tom:");
        List<CourseDTO> coursesAssignedToTom = courseService.getAllCoursesAssignedToTeacher(2);
        coursesAssignedToTom.forEach(mc -> System.out.println(mc.toString()));
        System.out.println("COURSES THAT HAVE SPECIFIC NUMBER OF ASSIGNED TEACHERS:");
        List<CourseDTO> courses = courseService.getCoursesWithNumberOfAssignedTeachers(3);
        courses.forEach(course -> System.out.println(course.toString()));
        System.out.println("TEACHERS SORTED BY AGE:");
        List<TeacherDTO> sortedTeachers = teacherService.getAllTeachersSortedBy(TeacherSortingCriteria.DESCENDING_BY_AGE);
        sortedTeachers.forEach(teacher -> System.out.println(teacher.toString()));
        System.out.println("COURSES THAT HAVEN'T BEEN STARTED:");
        List<CourseDTO> notStatedCourses = courseService.getFilteredCourses(CourseCriteria.NOT_STARTED);
        notStatedCourses.forEach(course -> System.out.println(course.toString()));
        System.out.println("FINISHED COURSES:");
        List<CourseDTO> finishedCourses = courseService.getFilteredCourses(CourseCriteria.FINISHED);
        finishedCourses.forEach(course -> System.out.println(course.toString()));
        System.out.println("ONGOING COURSES:");
        List<CourseDTO> ongoingCourses = courseService.getFilteredCourses(CourseCriteria.ONGOING);
        ongoingCourses.forEach(course -> System.out.println(course.toString()));
        System.out.println("COURSES THAT LAST SPECIFIC NUMBER OF DAYS:");
        List<CourseDTO> coursesThatLastNumberOfDays = courseService.getCoursesThatLast(5);
        coursesThatLastNumberOfDays.forEach(course -> System.out.println(course.toString()));
        System.out.println("\n=====================================================================\n");

        // ToDo: Implement console manipulator
    }
}
