create table member
(
    member_id      bigint generated by default as identity,
    member_role    varchar(255) not null,
    team_team_id   bigint       not null,
    member_user_id bigint       not null,
    primary key (member_id)
);
create table project
(
    project_id   bigint generated by default as identity,
    project_name varchar(255),
    primary key (project_id)
);
create table team
(
    team_id               bigint generated by default as identity,
    team_end_time         timestamp,
    team_location         varchar(255),
    team_max_member_count bigint not null,
    team_start_time       timestamp,
    team_status           varchar(255),
    team_project_id       bigint,
    primary key (team_id)
);
create table user
(
    user_id       bigint generated by default as identity,
    user_email    varchar(255) not null,
    user_fullname varchar(255) not null,
    user_nickname varchar(255) not null,
    user_picture  varchar(255),
    user_role     varchar(255) not null,
    primary key (user_id)
);
alter table member
    add constraint FKa04a95gj3stpfpgfofmg3f694 foreign key (team_team_id) references team;
alter table member
    add constraint FKj59gibu1r9jj1yub9gs5xf7il foreign key (member_user_id) references user;
alter table team
    add constraint FK5fbtjj3np8cwc128vo7wlm4ul foreign key (team_project_id) references project;