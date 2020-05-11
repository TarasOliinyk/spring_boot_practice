DROP TABLE IF EXISTS `course`;

CREATE TABLE `course` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `start_date` datetime(6) DEFAULT NULL,
  `end_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `teacher`;

CREATE TABLE `teacher` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `age` int(11) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `course_teachers`;

CREATE TABLE `course_teachers` (
  `courses_id` int(11) NOT NULL,
  `teachers_id` int(11) NOT NULL,
  CONSTRAINT `course_teachers_courses_fk` FOREIGN KEY (`courses_id`) REFERENCES `course` (`id`),
  CONSTRAINT `course_teachers_teachers_fk` FOREIGN KEY (`teachers_id`) REFERENCES `teacher` (`id`)
);