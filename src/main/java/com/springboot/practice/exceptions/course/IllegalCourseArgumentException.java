package com.springboot.practice.exceptions.course;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalCourseArgumentException extends RuntimeException {

    public IllegalCourseArgumentException() {
        super("IIllegal argument for Course object has been passed");
    }
}
