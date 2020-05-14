package com.springboot.practice.annotation;

import com.springboot.practice.data.Role;
import org.springframework.security.access.annotation.Secured;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Secured(Role.Name.ADMIN)
public @interface IsAdmin {
}
