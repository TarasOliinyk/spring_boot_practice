package com.springboot.practice.annotation.role;

import com.springboot.practice.data.UserRole;
import org.springframework.security.access.annotation.Secured;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Secured(UserRole.Name.STUDENT)
public @interface IsStudent {
}
