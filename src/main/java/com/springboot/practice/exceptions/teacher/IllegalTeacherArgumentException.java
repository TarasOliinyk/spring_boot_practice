package com.springboot.practice.exceptions.teacher;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalTeacherArgumentException extends RuntimeException {

    public IllegalTeacherArgumentException() {
        super("IIllegal argument for Teacher object has been passed");
    }
}
