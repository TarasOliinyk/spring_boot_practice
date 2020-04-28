package com.springboot.practice.utils.json.supplier.course;

import com.springboot.practice.utils.json.supplier.JsonDataSupplier;

public enum CourseJsonData implements JsonDataSupplier {
    COURSE_FOR_CREATE_ONE_POSITIVE("create_course_positive/courseData.json"),
    COURSE_FOR_CREATE_ONE_WITH_DATES_POSITIVE("create_course_positive/courseWithDatesData.json"),
    COURSE_FOR_UPDATE_COURSE("update_course/courseData.json"),
    COURSE_FOR_FIND_ONE_POSITIVE("find_one_positive/courseData.json"),
    COURSES_FOR_FIND_ALL_POSITIVE("find_all_positive/coursesData.json"),
    TEACHER_FOR_FIND_ALL_BY_TEACHERS_CONTAINING("find_all_by_teachers_containing/teacherData.json"),
    COURSES_FOR_FIND_ALL_BY_TEACHERS_CONTAINING("find_all_by_teachers_containing/coursesData.json"),
    TEACHER_FOR_ASSIGN_TEACHER_TO_COURSE("assign_teacher_to_course/teacherData.json"),
    COURSE_FOR_ASSIGN_TEACHER_TO_COURSE("assign_teacher_to_course/courseData.json"),
    TEACHERS_FOR_UNASSIGN_TEACHER_FROM_COURSE("unassign_teacher_from_course/teachersData.json"),
    COURSE_FOR_UNASSIGN_TEACHER_FROM_COURSE("unassign_teacher_from_course/courseData.json"),
    COURSES_FOR_FIND_ALL_BY_TEACHERS_COUNT("find_all_by_teachers_count/coursesData.json"),
    COURSES_FOR_FIND_ALL_BY_END_DATE_LESS_THAN("find_all_by_end_date_less_than/coursesData.json"),
    COURSES_FOR_FIND_ALL_THAT_LAST("find_all_that_last/coursesData.json");

    private String filePath;

    CourseJsonData(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getFilePath() {
        return "unit/service/course/" + this.filePath;
    }
}
