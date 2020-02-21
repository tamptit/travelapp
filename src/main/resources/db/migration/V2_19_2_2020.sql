-- create user table
CREATE TABLE travel.plan
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `name`        varchar(45) DEFAULT NULL,
    `content`     text,
    `active_user` int(11)     DEFAULT NULL,
    `follow_user` int(11)     DEFAULT NULL,
    `start_day`   date        DEFAULT NULL,
    `end_day`     date        DEFAULT NULL,
    `status`      varchar(45) DEFAULT NULL,
    PRIMARY KEY (`id`)
);
CREATE TABLE travel.plan_content
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `start_point` varchar(45) DEFAULT NULL,
    `end_point`   varchar(45) DEFAULT NULL,
    `vehicle`     varchar(45) DEFAULT NULL,
    `status`      varchar(45) DEFAULT NULL,
    `start_time`  time        DEFAULT NULL,
    `end_time`    time        DEFAULT NULL,
    `plan_id`     int(11)     DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_Content_Plan_idx` (`plan_id`),
    CONSTRAINT `FK_Content_Plan` FOREIGN KEY (`plan_id`) REFERENCES travel.plan (`id`)
);
CREATE TABLE travel.comment
(
    `id`      int(11) NOT NULL AUTO_INCREMENT,
    `content` text,
    `plan_id` int(11)     DEFAULT NULL,
    `user_id` bigint(20)  DEFAULT NULL,
    `status`  varchar(45) DEFAULT NULL,
    `time`    date        DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_Cmt_User_idx` (`user_id`),
    KEY `FK_Cmt_Plans_idx` (`plan_id`),
    CONSTRAINT `FK_Cmt_Plans` FOREIGN KEY (`plan_id`) REFERENCES travel.plan (`id`),
    CONSTRAINT `FK_Cmt_User` FOREIGN KEY (`user_id`) REFERENCES travel.user (`id`)
);
insert into travel.plan (`id`, `name`, `content`, `active_user`, `follow_user`, `start_day`, `end_day`, `status`) VALUES ('1', 'Hà Nội đến Hồ Chí Minh', 'Demo', '65', '150', '2020-12-12', '2020-12-05', 'active');
insert into travel.plan (`id`, `name`, `content`, `active_user`, `follow_user`, `start_day`, `end_day`, `status`) VALUES ('2', 'Hà Nội đến Lào Cai', 'Demo', '65', '200', '2020-12-12', '2020-12-05', 'latest');
insert into travel.plan (`id`, `name`, `content`, `active_user`, `follow_user`, `start_day`, `end_day`, `status`) VALUES ('3', 'Hà Nội đến Hải Dương', 'Demo', '50', '300', '2020-12-12', '2020-12-05', 'latest');
insert into travel.plan (`id`, `name`, `content`, `active_user`, `follow_user`, `start_day`, `end_day`, `status`) VALUES ('4', 'Hà Nội đến Thái Bình', 'Demo', '100', '150', '2020-12-12', '2020-12-05', 'latest');
insert into travel.plan (`id`, `name`, `content`, `active_user`, `follow_user`, `start_day`, `end_day`, `status`) VALUES ('5', 'Hà Nội đến Cao Bằng', 'Demo', '80', '150', '2020-12-12', '2020-12-05', 'latest');
insert into travel.plan (`id`, `name`, `content`, `active_user`, `follow_user`, `start_day`, `end_day`, `status`) VALUES ('6', 'Hà Nội đến Nam Định', 'Demo', '69', '200', '2020-12-12', '2020-12-05', 'latest');
insert into travel.plan (`id`, `name`, `content`, `active_user`, `follow_user`, `start_day`, `end_day`, `status`) VALUES ('7', 'Hà Nội đến Quảng Ninh', 'Demo', '96', '200', '2020-12-12', '2020-12-05', 'latest');
insert into travel.plan (`id`, `name`, `content`, `active_user`, `follow_user`, `start_day`, `end_day`, `status`) VALUES ('8', 'Hồ Chí Minh đến Đà Nẵng', 'Demo', '45', '145', '2020-12-12', '2020-12-05', 'latest');
insert into travel.plan (`id`, `name`, `content`, `active_user`, `follow_user`, `start_day`, `end_day`, `status`) VALUES ('9', 'Hồ Chí Minh đến Hà Nội', 'Demo', '65', '120', '2020-12-12', '2020-12-05', 'latest');
insert into travel.plan (`id`, `name`, `content`, `active_user`, `follow_user`, `start_day`, `end_day`, `status`) VALUES ('10', 'Hồ Chí Minh đến Hải Phòng', 'Demo', '85', '105', '2020-12-12', '2020-12-05', 'active');
insert into travel.plan (`id`, `name`, `content`, `active_user`, `follow_user`, `start_day`, `end_day`, `status`) VALUES ('11', 'Hồ Chí Minh đến Quảng Ninh', 'Demo', '78', '79', '2020-12-12', '2020-12-05', 'active');
insert into travel.plan (`id`, `name`, `content`, `active_user`, `follow_user`, `start_day`, `end_day`, `status`) VALUES ('12', 'Hồ Chí Minh đến Bắc Ninh', 'Demo', '90', '100', '2020-12-12', '2020-12-05', 'active');