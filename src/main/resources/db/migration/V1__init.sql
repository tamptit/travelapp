-- create user table
create table public.users
(
    id bigserial primary key,
    username   varchar(50)  not null,
    full_name  varchar(100) DEFAULT NULL,
    email      varchar(100) not null,
    password   varchar(50)  not null,
    date_birth date DEFAULT NULL,
    gender     smallint     not null,

    constraint user_email_uindex
        unique (email),
    constraint user_username_uindex
        unique (username)
);

INSERT INTO public.users(
	 username, full_name, email, password, date_birth, gender)
	VALUES ( 'nam.tran001', 'Tran Hoang Nam', 'nam.tran001@gmail', 'namtran001', '2020-02-18', 1);
INSERT INTO public.users(
	 username, full_name, email, password, date_birth, gender)
	VALUES ( 'nam.tran002', 'Tran Hoang Nam', 'nam.tran002@gmail', 'namtran002', '2020-02-19', 1);
INSERT INTO public.users(
     username, full_name, email, password, date_birth, gender)
    VALUES ( 'nam.hoang003', 'Nguyen Hoang Nam', 'nam.hoang003@gmail', 'namhoang003', '2020-02-20', 0);
INSERT INTO public.users(
     username, full_name, email, password, date_birth, gender)
    VALUES ( 'nam.pham004', 'Pham Hoang Nam', 'nam.pham004@gmail', 'nampham004', '2020-02-21', 0);
INSERT INTO public.users(
     username, full_name, email, password, date_birth, gender)
    VALUES ( 'nam.hoang005', 'Le Hoang Nam', 'nam.hoangle005@gmail', 'namhoangle005', '2020-02-22', 0);
INSERT INTO public.users( username, full_name, email, password, date_birth, gender)
	VALUES ('username123', 'tranhoangnam', 'namtran123@gamil', 'qweasd', '2019-09-10', 1);

