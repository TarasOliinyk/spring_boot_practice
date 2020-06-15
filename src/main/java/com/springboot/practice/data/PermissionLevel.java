package com.springboot.practice.data;

public enum PermissionLevel {
    USER_CREATE,
    USER_READ,
    USER_UPDATE,
    USER_DELETE,

    STUDENT_CREATE,
    STUDENT_READ,
    STUDENT_UPDATE,
    STUDENT_DELETE,

    TEACHER_CREATE,
    TEACHER_READ,
    TEACHER_UPDATE,
    TEACHER_DELETE,

    COURSE_CREATE,
    COURSE_READ,
    COURSE_UPDATE,
    COURSE_DELETE;

    public interface Name {
        String USER_CREATE = "USER_CREATE";
        String USER_READ = "USER_READ";
        String USER_UPDATE = "USER_UPDATE";
        String USER_DELETE = "USER_DELETE";

        String STUDENT_CREATE = "STUDENT_CREATE";
        String STUDENT_READ = "STUDENT_READ";
        String STUDENT_UPDATE = "STUDENT_UPDATE";
        String STUDENT_DELETE = "STUDENT_DELETE";

        String TEACHER_CREATE = "TEACHER_CREATE";
        String TEACHER_READ = "TEACHER_READ";
        String TEACHER_UPDATE = "TEACHER_UPDATE";
        String TEACHER_DELETE = "TEACHER_DELETE";

        String COURSE_CREATE = "COURSE_CREATE";
        String COURSE_READ = "COURSE_READ";
        String COURSE_UPDATE = "COURSE_UPDATE";
        String COURSE_DELETE = "COURSE_DELETE";
    }
}
