INSERT INTO `role`
(name)
VALUES
('ROLE_USER'),
('ROLE_STUDENT'),
('ROLE_TEACHER'),
('ROLE_ADMIN');

INSERT INTO `permission`
(name)
VALUES
('USER_CREATE'),
('USER_READ'),
('USER_UPDATE'),
('USER_DELETE'),
('STUDENT_CREATE'),
('STUDENT_READ'),
('STUDENT_UPDATE'),
('STUDENT_DELETE'),
('TEACHER_CREATE'),
('TEACHER_READ'),
('TEACHER_UPDATE'),
('TEACHER_DELETE'),
('COURSE_CREATE'),
('COURSE_READ'),
('COURSE_UPDATE'),
('COURSE_DELETE');

INSERT INTO `role_permissions` VALUES
(1,1),
(1,2),
(1,6),
(1,10),
(1,14),

(2,2),
(2,5),
(2,6),
(2,10),
(2,14),

(3,2),
(3,6),
(3,9),
(3,10),
(3,14),

(4,1),
(4,2),
(4,3),
(4,4),
(4,5),
(4,6),
(4,7),
(4,8),
(4,9),
(4,10),
(4,11),
(4,12),
(4,13),
(4,14),
(4,15),
(4,16);