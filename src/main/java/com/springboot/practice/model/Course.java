package com.springboot.practice.model;

import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.springboot.practice.utils.LocalDatePersistenceConverter;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"teachers", "students", "startDate", "endDate"})
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

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

    @ManyToMany
    private List<Teacher> teachers = new ArrayList<>();

    @ManyToMany
    private List<Student> students = new ArrayList<>();

    public Course(String name) {
        this.name = name;
    }

    public Course(String name, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
