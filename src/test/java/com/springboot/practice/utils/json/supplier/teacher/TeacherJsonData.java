package com.springboot.practice.utils.json.supplier.teacher;

import com.springboot.practice.utils.json.supplier.JsonDataSupplier;

public enum TeacherJsonData implements JsonDataSupplier {
    TEACHER_FOR_CREATE_ONE_POSITIVE( "create_teacher_positive/teacherData.json"),
    TEACHER_FOR_FIND_ONE_POSITIVE("find_one_positive/teacherData.json"),
    TEACHERS_FOR_FIND_ALL_POSITIVE("find_all_positive/teachersData.json"),
    TEACHERS_FOR_FIND_ALL_SORTED_BY_AGE_ASC("find_all_sorted/by_age_asc/teachersData.json"),
    COURSE_FOR_FIND_ALL_BY_COURSES_CONTAINING("find_all_by_courses_containing/courseData.json"),
    TEACHERS_FOR_FIND_ALL_BY_COURSES_CONTAINING("find_all_by_courses_containing/teachersData.json");

    private String filePath;

    TeacherJsonData(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getFilePath() {
        return "testdata/service/teacher/" + this.filePath;
    }
}
