/*User*/
insert into user (id, email, fullname, nickname, picture, role)
values (1, 'tester001@gmail.com', 'tester001', 'test1', 'none', 'USER');
insert into user (id, email, fullname, nickname, picture, role)
values (2, 'tester002@gmail.com', 'tester002', 'test2', 'none', 'USER');
insert into user (id, email, fullname, nickname, picture, role)
values (3, 'tester003@gmail.com', 'tester003', 'test3', 'none', 'USER');
insert into user (id, email, fullname, nickname, picture, role)
values (4, 'tester004@gmail.com', 'tester004', 'test4', 'none', 'USER');
insert into user (id, email, fullname, nickname, picture, role)
values (5, 'tester005@gmail.com', 'tester005', 'test5', 'none', 'USER');
insert into user (id, email, fullname, nickname, picture, role)
values (6, 'tester006@gmail.com', 'tester006', 'test6', 'none', 'USER');
insert into user (id, email, fullname, nickname, picture, role)
values (7, 'hwon@student.42seoul.kr', 'Hyunjin Won', 'hwon', 'https://cdn.intra.42.fr/users/hwon.jpg', 'ADMIN');
insert into user (id, email, fullname, nickname, picture, role)
values (8, 'ychoi@student.42seoul.kr', 'Youngho Choi', 'ychoi', 'https://cdn.intra.42.fr/users/ychoi.jpg', 'ADMIN');
insert into user (id, email, fullname, nickname, picture, role)
values (9, 'minkang@student.42seoul.kr', 'Mincheol Kang', 'minkang', 'https://cdn.intra.42.fr/users/minkang.jpg',
        'ADMIN');

/*project*/
-- 순서 부여 기준은 시계방향 입니다.

-- custom
insert into project (id, name)
values (1, 'custom');

-- 0 circle
insert into project (id, name)
values (2, 'Libft');

-- 1 circle
insert into project (id, name)
values (3, 'ft_printf');
insert into project (id, name)
values (4, 'get_next_line');
insert into project (id, name)
values (5, 'Born2beroot');

-- 2 circle
insert into project (id, name)
values (6, 'minitalk');
insert into project (id, name)
values (7, 'pipex');
insert into project (id, name)
values (8, 'so_long');
insert into project (id, name)
values (9, 'FdF');
insert into project (id, name)
values (10, 'fract-ol');
insert into project (id, name)
values (11, 'push_swap');

-- 3 circle
insert into project (id, name)
values (12, 'minishell');
insert into project (id, name)
values (13, 'Philosophers');

-- 4 circle
insert into project (id, name)
values (14, 'CPP Module 00');
insert into project (id, name)
values (15, 'CPP Module 01');
insert into project (id, name)
values (16, 'CPP Module 02');
insert into project (id, name)
values (17, 'CPP Module 03');
insert into project (id, name)
values (18, 'CPP Module 04');
insert into project (id, name)
values (19, 'CPP Module 05');
insert into project (id, name)
values (20, 'CPP Module 06');
insert into project (id, name)
values (21, 'CPP Module 07');
insert into project (id, name)
values (22, 'CPP Module 08');
insert into project (id, name)
values (23, 'NetPractice');
insert into project (id, name)
values (24, 'cub3d');
insert into project (id, name)
values (25, 'miniRT');

-- 5 circle
insert into project (id, name)
values (26, 'Inception');
insert into project (id, name)
values (27, 'ft_containers');
insert into project (id, name)
values (28, 'webserv');
insert into project (id, name)
values (29, 'ft_irc');

-- 6 circle
insert into project (id, name)
values (30, 'ft_transcendence');


/*Team*/
insert into team (id, start_time, end_time, location, max_member_count, status,
                  project_id, subject, description)
values (1, '2022-08-03 13:00:00', '2022-08-03 15:00:00', 'GAEPO', 3, 'READY', 2, '제목입니다', '설명입니다');
insert into team (id, start_time, end_time, location, max_member_count, status,
                  project_id, subject, description)
values (2, '2022-08-04 13:00:00', '2022-08-04 18:00:00', 'ONLINE', 4, 'WAITING', 3, '제목입니다', '설명입니다');
insert into team (id, start_time, end_time, location, max_member_count, status,
                  project_id, subject, description)
values (3, '2022-08-04 11:00:00', '2022-08-04 19:00:00', 'SEOCHO', 4, 'READY', 4, '제목입니다', '설명입니다');
insert into team (id, start_time, end_time, location, max_member_count, status,
                  project_id, subject, description)
values (4, '2022-08-04 13:00:00', '2022-08-04 15:00:00', 'GAEPO', 4, 'WAITING', 2, '제목입니다', '설명입니다');
insert into team (id, start_time, end_time, location, max_member_count, status,
                  project_id, subject, description)
values (5, '2022-08-01 13:00:00', '2022-08-01 15:00:00', 'ONLINE', 3, 'FULL', 2, '제목입니다', '설명입니다');
insert into team (id, start_time, end_time, location, max_member_count, status,
                  project_id, subject, description)
values (6, '2022-08-06 15:00:00', '2022-08-06 16:00:00', 'GAEPO', 3, 'WAITING', 3, '제목입니다', '설명입니다');
insert into team (id, start_time, end_time, location, max_member_count, status,
                  project_id, subject, description)
values (7, '2022-08-10 13:00:00', '2022-08-10 15:00:00', 'ONLINE', 10, 'READY', 4, '제목입니다', '설명입니다');
insert into team (id, start_time, end_time, location, max_member_count, status,
                  project_id, subject, description)
values (8, '2022-08-10 12:00:00', '2022-08-10 15:00:00', 'ONLINE', 3, 'WAITING', 4, '제목입니다', '설명입니다');
insert into team (id, start_time, end_time, location, max_member_count, status,
                  project_id, subject, description)
values (9, '2022-08-30 09:00:00', '2022-08-30 12:00:00', 'ONLINE', 3, 'END', 1, '제목입니다', '설명입니다');
insert into team (id, start_time, end_time, location, max_member_count, status,
                  project_id, subject, description)
values (10, '2022-08-22 09:00:00', '2022-08-22 12:00:00', 'ONLINE', 2, 'WAITING', 1, '제목입니다', '설명입니다');

/*member*/
insert into member (id, team_id, user_id, role, creator, participation)
values (1, 1, 1, 'MENTEE', 1, 1);
insert into member (id, team_id, user_id, role, creator, participation)
values (2, 1, 2, 'MENTOR', 0, 1);
insert into member (id, team_id, user_id, role, creator, participation)
values (3, 2, 1, 'MENTEE', 1, 1);
insert into member (id, team_id, user_id, role, creator, participation)
values (4, 3, 1, 'MENTEE', 1, 1);
insert into member (id, team_id, user_id, role, creator, participation)
values (5, 3, 2, 'MENTEE', 0, 1);
insert into member (id, team_id, user_id, role, creator, participation)
values (6, 3, 3, 'MENTOR', 0, 1);
insert into member (id, team_id, user_id, role, creator, participation)
values (7, 3, 4, 'MENTEE', 0, 1);
insert into member (id, team_id, user_id, role, creator, participation)
values (8, 4, 5, 'MENTEE', 1, 1);
insert into member (id, team_id, user_id, role, creator, participation)
values (9, 5, 1, 'MENTOR', 0, 1);
insert into member (id, team_id, user_id, role, creator, participation)
values (10, 5, 2, 'MENTEE', 1, 1);
insert into member (id, team_id, user_id, role, creator, participation)
values (11, 5, 5, 'MENTEE', 0, 1);
insert into member (id, team_id, user_id, role, creator, participation)
values (12, 6, 2, 'MENTEE', 1, 1);
insert into member (id, team_id, user_id, role, creator, participation)
values (13, 7, 1, 'MENTEE', 1, 1);
insert into member (id, team_id, user_id, role, creator, participation)
values (14, 7, 4, 'MENTOR', 0, 1);
insert into member (id, team_id, user_id, role, creator, participation)
values (15, 8, 2, 'MENTEE', 1, 1);
insert into member (id, team_id, user_id, role, creator, participation)
values (16, 9, 1, 'MENTEE', 1, 1);
insert into member (id, team_id, user_id, role, creator, participation)
values (17, 9, 2, 'MENTEE', 0, 1);
insert into member (id, team_id, user_id, role, creator, participation)
values (18, 9, 3, 'MENTOR', 0, 1);
insert into member (id, team_id, user_id, role, creator, participation)
values (19, 10, 4, 'MENTEE', 1, 1);