INSERT INTO `roles` (`id`, `description`, `name`) VALUES
(1, 'Akses role ADMIN', 'ROLE_ADMIN'),
(2, 'Akses role MEMBER', 'ROLE_MEMBER');
INSERT INTO `users` (`id`, `email`, `firstname`, `lastname`, `password`, `username`) VALUES
(1, 'peripurnama@gmail.com', 'peri', 'purnama', '$2a$10$11i1V7P937GDJXgXpVVAw.YP6ixT2TZlaEe61Wa5Sx1XBWS0yvBr2', 'peripurnama'),
(2, 'bejo@gmail.com', 'bang', 'bejo', '$2a$10$X2pOk5Lp4bOQUjs9nMkOhOY.GbOR5rI.FCfWkaB.SIpLl/BX3rDSO', 'bejo');
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES
(1, 1),
(2, 2);

-- password: @Asdf123