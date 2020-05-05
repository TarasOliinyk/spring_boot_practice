package com.springboot.practice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.springboot.practice.utils.LocalDatePersistenceConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString(exclude = {"teachers", "students"})
@EqualsAndHashCode(exclude = {"teachers", "students", "startDate", "endDate"})
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @NotEmpty(message = "Course cannot be registered without a name")
    private String name;

    @JsonIgnore
    @ManyToMany
    private List<Teacher> teachers = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    private List<Student> students = new ArrayList<>();

    @Column
    @Convert(converter = LocalDatePersistenceConverter.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // This is not obligatory since the LocalDate takes care of validating the obtained date in a String format
    private LocalDate startDate;

    @Column
    @Convert(converter = LocalDatePersistenceConverter.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate endDate;

    public Course(String name) {
        this.name = name;
    }

    public Course(String name, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
