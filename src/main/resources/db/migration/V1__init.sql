-- create user table
create table travel.user
(
    id bigint auto_increment primary key,
    username varchar(50)  not null,
    full_name varchar(100) DEFAULT NULL,
    email    varchar(100) not null,
    password varchar(50)  not null,
    date_birth date DEFAULT NULL,
    gender tinyint(4) not null,
    role_id  int          ,
    constraint user_email_uindex
        unique (email),
    constraint user_username_uindex
        unique (username)
);
