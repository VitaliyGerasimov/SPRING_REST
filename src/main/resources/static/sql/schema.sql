CREATE TABLE IF NOT EXISTS `user`
(
    `id`        int(11)      NOT NULL AUTO_INCREMENT,
    `last_Name` varchar(255) NOT NULL,
    `name`      varchar(255) NOT NULL,
    `password`  varchar(255) NOT NULL,
    `email`     varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
);


CREATE TABLE IF NOT EXISTS `role`
(
    `id`   int(11)      NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
);


CREATE TABLE IF NOT EXISTS `users_roles`
(
    `user_id` int(11) NOT NULL,
    `role_id` int(11) NOT NULL,
    PRIMARY KEY (`user_id`, `role_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
);