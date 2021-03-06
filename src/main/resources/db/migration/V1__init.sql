-- create user table
create table public.users
(
    id bigserial primary key,
    username    varchar(50)  not null,
    full_name   varchar(100) DEFAULT NULL,
    email       varchar(100) not null,
    password    varchar(100)  not null,
    date_birth  date DEFAULT NULL,
    gender     boolean     not null,

    constraint user_email_uindex
        unique (email),
    constraint user_username_uindex
        unique (username)
);


