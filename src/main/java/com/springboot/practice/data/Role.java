package com.springboot.practice.data;

public enum Role {
    USER(Name.USER),
    STUDENT(Name.STUDENT),
    TEACHER(Name.TEACHER),
    ADMIN(Name.ADMIN);

    private String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public interface Name {
        String USER = "ROLE_USER";
        String STUDENT = "ROLE_STUDENT";
        String TEACHER = "ROLE_TEACHER";
        String ADMIN = "ROLE_ADMIN";
    }
}
