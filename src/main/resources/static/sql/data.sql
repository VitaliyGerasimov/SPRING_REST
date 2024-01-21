INSERT INTO `user` (`id`, `last_Name`, `name`, `password`,`email`)
VALUES
    (1,'admin','admin','admin','admin@mail.ru'),
    (2,'user','user','user','user@mail.ru');

INSERT INTO `role` (`id`, `name`)
VALUES
    (1,'ROLE_ADMIN'),
    (2,'ROLE_USER');

INSERT INTO `users_roles`
VALUES
    (1, 1),
    (1, 2),
    (2, 2);