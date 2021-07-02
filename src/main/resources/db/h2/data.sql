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

/*project*/
insert into project (id, name)
values (1, 'custom');
insert into project (id, name)
values (2, 'libft');
insert into project (id, name)
values (3, 'ft_printf');
insert into project (id, name)
values (4, 'get_next_line');

/*Team*/
insert into team (id, start_time, end_time, location, max_member_count, status,
                  project_id)
values (1, '2021-07-03 13:00:00', '2021-07-03 15:00:00', 'GAEPO', 3, 'READY', 2);
insert into team (id, start_time, end_time, location, max_member_count, status,
                  project_id)
values (2, '2021-07-04 13:00:00', '2021-07-04 18:00:00', 'ONLINE', 4, 'WAITING', 3);
insert into team (id, start_time, end_time, location, max_member_count, status,
                  project_id)
values (3, '2021-07-04 11:00:00', '2021-07-04 19:00:00', 'SEOCHO', 4, 'READY', 4);
insert into team (id, start_time, end_time, location, max_member_count, status,
                  project_id)
values (4, '2021-07-04 13:00:00', '2021-07-04 15:00:00', 'GAEPO', 4, 'WAITING', 2);
insert into team (id, start_time, end_time, location, max_member_count, status,
                  project_id)
values (5, '2021-07-01 13:00:00', '2021-07-01 15:00:00', 'ONLINE', 3, 'RUNNING', 2);
insert into team (id, start_time, end_time, location, max_member_count, status,
                  project_id)
values (6, '2021-07-06 15:00:00', '2021-07-06 16:00:00', 'GAEPO', 3, 'WAITING', 3);
insert into team (id, start_time, end_time, location, max_member_count, status,
                  project_id)
values (7, '2021-07-10 13:00:00', '2021-07-10 15:00:00', 'ONLINE', 2, 'READY', 4);
insert into team (id, start_time, end_time, location, max_member_count, status,
                  project_id)
values (8, '2021-07-10 12:00:00', '2021-07-10 15:00:00', 'ONLINE', 3, 'WAITING', 4);
insert into team (id, start_time, end_time, location, max_member_count, status,
                  project_id)
values (9, '2021-06-30 09:00:00', '2021-06-30 12:00:00', 'ONLINE', 3, 'END', 1);

/*member*/
insert into member (id, team_id, user_id, role)
values (1, 1, 1, 'MENTEE');
insert into member (id, team_id, user_id, role)
values (2, 1, 2, 'MENTOR');
insert into member (id, team_id, user_id, role)
values (3, 2, 1, 'MENTEE');
insert into member (id, team_id, user_id, role)
values (4, 3, 1, 'MENTEE');
insert into member (id, team_id, user_id, role)
values (5, 3, 2, 'MENTEE');
insert into member (id, team_id, user_id, role)
values (6, 3, 3, 'MENTOR');
insert into member (id, team_id, user_id, role)
values (7, 3, 4, 'MENTEE');
insert into member (id, team_id, user_id, role)
values (8, 4, 5, 'MENTEE');
insert into member (id, team_id, user_id, role)
values (9, 5, 1, 'MENTOR');
insert into member (id, team_id, user_id, role)
values (10, 5, 2, 'MENTEE');
insert into member (id, team_id, user_id, role)
values (11, 5, 5, 'MENTEE');
insert into member (id, team_id, user_id, role)
values (12, 6, 2, 'MENTEE');
insert into member (id, team_id, user_id, role)
values (13, 7, 1, 'MENTEE');
insert into member (id, team_id, user_id, role)
values (14, 7, 4, 'MENTOR');
insert into member (id, team_id, user_id, role)
values (15, 8, 2, 'MENTEE');
insert into member (id, team_id, user_id, role)
values (16, 9, 1, 'MENTEE');
insert into member (id, team_id, user_id, role)
values (17, 9, 2, 'MENTEE');
insert into member (id, team_id, user_id, role)
values (18, 9, 3, 'MENTOR');
