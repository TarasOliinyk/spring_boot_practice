package com.springboot.practice.data;

public enum Role { // ToDo: Should be removed or modified to adapt to new approach with User Permissions
    ROLE_USER,
    ROLE_STUDENT,
    ROLE_TEACHER,
    ROLE_ADMIN;

    public interface Name {
        String USER = "ROLE_USER";
        String STUDENT = "ROLE_STUDENT";
        String TEACHER = "ROLE_TEACHER";
        String ADMIN = "ROLE_ADMIN";
    }
}
