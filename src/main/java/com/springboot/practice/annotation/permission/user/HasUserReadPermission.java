package com.springboot.practice.annotation.permission.user;

import com.springboot.practice.data.PermissionLevel;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('" + PermissionLevel.Name.USER_READ + "')")
public @interface HasUserReadPermission {
}
