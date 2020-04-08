import com.lits.config.JavaConfig;
import com.lits.model.Course;
import com.lits.model.Teacher;
import com.lits.service.CourseService;
import com.lits.service.TeacherService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class);
        TeacherService teacherService = context.getBean(TeacherService.class);
        CourseService courseService = context.getBean(CourseService.class);

//======================================================= Teacher ======================================================

//        Course course = new Course();
//        courseA.setId(1);
//        courseA.setName("Math");
//
//        // Add teacher
//        Teacher teacher = new Teacher();
//        teacher.setFirstName("Bob");
//        teacher.setLastName("Green");
//        teacher.getCourses().add(course);
//        teacherService.addTeacher(teacher);

        // Get teacher:
//        Teacher teacher = teacherService.getTeacher(1);
//        System.out.println(teacher.toString());

        // Get all teachers:
//        List<Teacher> teachers = teacherService.getAllTeachers();
//        teachers.forEach(teacher -> System.out.println(teacher.toString()));

        // Update teacher:
//        Teacher teacher = new Teacher();
//        teacher.setId(3);
//        teacher.setFirstName("Tom");
//        teacher.setLastName("Green");
//        Teacher updatedTeacher = teacherService.updateTeacher(teacher);
//        System.out.println(updatedTeacher.toString());
//
        // Delete teacher:
//        teacherService.deleteTeacher(5);

        // Add course to teacher:
//        Course course = courseService.getCourse(2);
//        teacherService.addCourseToTeacher(1, course);

        // Get courses assigned to teacher:
//        List<Course> courses = teacherService.getCoursesAssignedToTeacher(1);
//        courses.forEach(course -> System.out.println(course.toString()));

        // Get number of courses assigned to teacher:
//        int numberOfCourses = teacherService.getNumberOfCoursesAssignedToTeacher(3);
//        System.out.println("Number of courses assigned to teacher: " + numberOfCourses);

        // Delete course assigned to teacher:
//        Course course = courseService.getCourse(2);
//        teacherService.deleteCourseAssignedToTeacher(1, course);

        // Reassign course from one teacher to another:
//        Course course = courseService.getCourse(1);
//        teacherService.reassignCourse(course, 1, 3); // ToDo: Doesn't work - need lecture's help w/ this one

//======================================================= Course =======================================================

         // Add course:
//        Teacher teacher = new Teacher();
//        teacher.setId(1);
//        Course course = new Course();
//        course.setName("Law");
//        course.setTeacher(teacher);
//        courseService.addCourse(course);
//
        // Get course:
//        Course course = courseService.getCourse(1);
//        System.out.println(course.toString());

        // Get all courses:
//        List<Course> courses = courseService.getAllCourses();
//        courses.forEach(course -> System.out.println(course.toString()));

        // Get all courses without assigned teacher:
//        List<Course> courses = courseService.getAllCoursesWithoutAssignedTeacher();
//        courses.forEach(course -> System.out.println(course.toString()));

        // Update course:
//        Course course = new Course();
//        course.setId(2);
//        course.setName("Programming");
//        Course updateCourse = courseService.updateCourse(course);
//        System.out.println(updateCourse.toString());

        // Delete Course:
//        courseService.deleteCourse(2);
    }
}
