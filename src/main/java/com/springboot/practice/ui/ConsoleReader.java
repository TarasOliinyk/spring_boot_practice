package com.springboot.practice.ui;

import com.springboot.practice.model.Course;
import com.springboot.practice.model.Teacher;
import com.springboot.practice.service.CourseService;
import com.springboot.practice.service.TeacherService;
import com.springboot.practice.service.criteria.TeacherSortingParameter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
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

        Course math = courseService.getCourse(1);
        Course physics = courseService.getCourse(2);
        Teacher teacherMike = teacherService.getTeacher(1);
        Teacher teacherTom = teacherService.getTeacher(2);
        Teacher teacherBob = teacherService.getTeacher(3);

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
        List<Course> coursesAssignedToMike = teacherService.getAllCoursesAssignedToTeacher(teacherMike);
        coursesAssignedToMike.forEach(mc -> System.out.println(mc.toString()));
        System.out.println("COURSES ASSIGNED TO Tom:");
        List<Course> coursesAssignedToTom = teacherService.getAllCoursesAssignedToTeacher(teacherTom);
        coursesAssignedToTom.forEach(mc -> System.out.println(mc.toString()));
        System.out.println("COURSES THAT HAVE SPECIFIC NUMBER OF ASSIGNED TEACHERS:");
        List<Course> courses = courseService.getCoursesWithNumberOfAssignedTeachers(3);
        courses.forEach(course -> System.out.println(course.toString()));
        System.out.println("TEACHERS SORTED BY AGE:");
        List<Teacher> sortedTeachers = teacherService.getAllTeachersSortedBy(TeacherSortingParameter.DESCENDING_BY_AGE);
        sortedTeachers.forEach(teacher -> System.out.println(teacher.toString()));
        System.out.println("COURSES THAT HAVEN'T BEEN STARTED:");
        List<Course> notStatedCourses = courseService.getNotStartedCourses();
        notStatedCourses.forEach(course -> System.out.println(course.toString()));
        System.out.println("FINISHED COURSES:");
        List<Course> finishedCourses = courseService.getFinishedCourses();
        finishedCourses.forEach(course -> System.out.println(course.toString()));
        System.out.println("ONGOING COURSES:");
        List<Course> ongoingCourses = courseService.getOngoingCourses();
        ongoingCourses.forEach(course -> System.out.println(course.toString()));
        System.out.println("COURSES THAT LAST SPECIFIC NUMBER OF DAYS:");
        List<Course> coursesThatLastNumberOfDays = courseService.getCoursesThatLast(5);
        coursesThatLastNumberOfDays.forEach(course -> System.out.println(course.toString()));
        System.out.println("\n=====================================================================\n");

        // ToDo: Implement console manipulator
    }
}
