DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `user_roles`;

CREATE TABLE `user_roles` (
  `users_id` int(11) NOT NULL,
  `roles_id` int(11) NOT NULL,
  CONSTRAINT `user_roles_user_fk` FOREIGN KEY (`users_id`) REFERENCES `user` (`id`),
  CONSTRAINT `user_roles_role_fk` FOREIGN KEY (`roles_id`) REFERENCES `role` (`id`)
);

DROP TABLE IF EXISTS `permission`;

CREATE TABLE `permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `role_permissions`;

CREATE TABLE `role_permissions` (
  `roles_id` int(11) NOT NULL,
  `permissions_id` int(11) NOT NULL,
  CONSTRAINT `role_permissions_role_fk` FOREIGN KEY (`roles_id`) REFERENCES `role` (`id`),
  CONSTRAINT `role_permissions_permission_fk` FOREIGN KEY (`permissions_id`) REFERENCES `permission` (`id`)
);