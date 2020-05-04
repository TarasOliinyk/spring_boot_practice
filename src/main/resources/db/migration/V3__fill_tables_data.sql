INSERT INTO `course`
(name, start_date, end_date)
VALUES
('Math','2020-04-04','2020-04-11'),
('Physics','2020-03-30','2020-04-04'),
('Law','2020-04-14','2020-04-24'),
('TEST',NULL,NULL);

INSERT INTO `teacher`
(age, first_name, last_name)
VALUES
(35,'Mike','Brown'),
(40,'Tom','Green'),
(37,'Bob','White');

INSERT INTO `course_teachers` VALUES
(1,1),
(2,2),
(2,1),
(2,3);
