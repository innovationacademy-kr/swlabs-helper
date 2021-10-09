CREATE TABLE `member`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT,
    `role`          varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `team_id`       bigint(20) NOT NULL,
    `user_id`       bigint(20) NOT NULL,
    `creator`       bit(1)                                  NOT NULL,
    `participation` bit(1),
    `created`       timestamp,
    `updated`       timestamp,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 127
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `project`
(
    `id`   bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 31
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `team`
(
    `id`               bigint(20) NOT NULL AUTO_INCREMENT,
    `end_time`         datetime(6) DEFAULT NULL,
    `location`         varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `max_member_count` bigint(20) NOT NULL,
    `start_time`       datetime(6) DEFAULT NULL,
    `status`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `project_id`       bigint(20) DEFAULT NULL,
    `subject`          varchar(255)                            DEFAULT NULL,
    `description`      text COLLATE utf8mb4_unicode_ci         DEFAULT NULL,
    `created`          timestamp,
    `updated`          timestamp,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 55
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `user`
(
    `id`       bigint(20) NOT NULL AUTO_INCREMENT,
    `email`    varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `fullname` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `nickname` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `picture`  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `role`     varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created`  timestamp,
    `updated`  timestamp,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 40
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `review`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `team_id`     bigint,
    `user_id`     bigint,
    `description` text COLLATE utf8mb4_unicode_ci,
    `fun`         int,
    `interested`  int,
    `nice`        int,
    `time`        int,
    `status`      varchar(255),
    `created`     timestamp,
    `updated`     timestamp,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 40
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table settle
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `admin_id`    bigint(20) NOT NULL,
    `review_id`   bigint(20) NOT NULL,
    `status`      varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `wallet_paid` bit(1),
    `created`     timestamp,
    `updated`     timestamp,
    primary key (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 31
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

ALTER TABLE member
    ADD CONSTRAINT FK_MEMBER_TEAM FOREIGN KEY (team_id) REFERENCES team (id);
ALTER TABLE member
    ADD CONSTRAINT FK_MEMBER_USER FOREIGN KEY (user_id) REFERENCES user (id);
ALTER TABLE team
    ADD CONSTRAINT FK_TEAM_PROJECT FOREIGN KEY (project_id) REFERENCES project (id);
ALTER TABLE review
    ADD CONSTRAINT FK_REVIEW_TEAM FOREIGN KEY (team_id) REFERENCES team (id);
ALTER TABLE review
    ADD CONSTRAINT FK_REVIEW_USER FOREIGN KEY (user_id) REFERENCES user (id);
ALTER TABLE settle
    ADD CONSTRAINT FK_SETTLE_REVIEW FOREIGN KEY (review_id) REFERENCES review (id);
ALTER TABLE settle
    ADD CONSTRAINT FK_SETTLE_ADMIN FOREIGN KEY (admin_id) REFERENCES user (id);