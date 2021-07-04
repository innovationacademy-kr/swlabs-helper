create table member
(
    id      bigint generated by default as identity,
    role    varchar(255) not null,
    team_id bigint       not null,
    user_id bigint       not null,
    creator smallint(1) not null,
    primary key (id)
);
create table project
(
    id   bigint generated by default as identity,
    name varchar(255),
    primary key (id)
);
create table team
(
    id               bigint generated by default as identity,
    end_time         timestamp,
    location         varchar(255),
    max_member_count bigint not null,
    start_time       timestamp,
    status           varchar(255),
    project_id       bigint,
    primary key (id)
);
create table user
(
    id       bigint generated by default as identity,
    email    varchar(255) not null,
    fullname varchar(255) not null,
    nickname varchar(255) not null,
    picture  varchar(255),
    role     varchar(255) not null,
    primary key (id)
);
alter table member
    add constraint FKa04a95gj3stpfpgfofmg3f694 foreign key (team_id) references team;
alter table member
    add constraint FKj59gibu1r9jj1yub9gs5xf7il foreign key (user_id) references user;
alter table team
    add constraint FK5fbtjj3np8cwc128vo7wlm4ul foreign key (project_id) references project;