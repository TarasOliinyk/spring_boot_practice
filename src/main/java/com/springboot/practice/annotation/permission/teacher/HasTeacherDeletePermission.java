package com.springboot.practice.annotation.permission.teacher;

import com.springboot.practice.data.PermissionLevel;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('" + PermissionLevel.Name.TEACHER_DELETE + "')")
public @interface HasTeacherDeletePermission {
}
