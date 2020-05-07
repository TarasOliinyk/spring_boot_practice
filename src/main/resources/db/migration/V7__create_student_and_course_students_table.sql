DROP TABLE IF EXISTS `student`;

CREATE TABLE `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `course_students`;

CREATE TABLE `course_students` (
  `courses_id` int(11) NOT NULL,
  `students_id` int(11) NOT NULL,
  CONSTRAINT `course_students_courses_fk` FOREIGN KEY (`courses_id`) REFERENCES `course` (`id`),
  CONSTRAINT `course_students_students_fk` FOREIGN KEY (`students_id`) REFERENCES `student` (`id`)
);