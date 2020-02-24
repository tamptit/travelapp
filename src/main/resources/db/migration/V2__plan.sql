-- create user table
CREATE TABLE public.plan
(
    id bigserial primary key,
    name        varchar(45) DEFAULT NULL,
    content     text,
    active_user INT         DEFAULT NULL,
    follow_user INT         DEFAULT NULL,
    start_day   date        DEFAULT NULL,
    end_day     date        DEFAULT NULL,
    status      varchar(45) DEFAULT NULL
);

CREATE TABLE public.plan_content
(
    id bigserial primary key,
    start_point varchar(45) DEFAULT NULL,
    end_point   varchar(45) DEFAULT NULL,
    vehicle     varchar(45) DEFAULT NULL,
    status      varchar(45) DEFAULT NULL,
    start_time  time        DEFAULT NULL,
    end_time    time        DEFAULT NULL,
    plan_id     INT REFERENCES plan (id)
);
CREATE TABLE public.comment
(
    id bigserial primary key,
    content text,
    status  varchar(45) DEFAULT NULL,
    time    date        DEFAULT NULL,
    user_id INT REFERENCES users (id),
    plan_id INT REFERENCES plan (id)
);

INSERT INTO public.plan(
	id, name, content, active_user, follow_user, start_day, end_day, status)
	VALUES ('11', 'Ha Noi to HCM', 'Demo', '65', '150', '2020-12-12', '2020-12-05', 'active');
INSERT INTO public.plan(
	id, name, content, active_user, follow_user, start_day, end_day, status)
	VALUES ('2', 'Ha Noi to Lao Cai', 'Demo', '65', '200', '2020-12-12', '2020-12-05', 'latest');
INSERT INTO public.plan(
	id, name, content, active_user, follow_user, start_day, end_day, status)
	VALUES ('3', 'Ha Noi to Hai Duong', 'Demo', '50', '300', '2020-12-12', '2020-12-05', 'latest');
INSERT INTO public.plan(
	id, name, content, active_user, follow_user, start_day, end_day, status)
	VALUES ('4', 'Ha Noi to Thai Binh', 'Demo', '100', '150', '2020-12-12', '2020-12-05', 'latest');
INSERT INTO public.plan(
	id, name, content, active_user, follow_user, start_day, end_day, status)
	VALUES ('5', 'Ha Noi to Dien Bien', 'Demo', '80', '150', '2020-12-12', '2020-12-05', 'latest');
INSERT INTO public.plan(
	id, name, content, active_user, follow_user, start_day, end_day, status)
	VALUES ('6', 'Ha Noi to Ha Giang', 'Demo', '69', '200', '2020-12-12', '2020-12-05', 'latest');
INSERT INTO public.plan(
	id, name, content, active_user, follow_user, start_day, end_day, status)
	VALUES ('7', 'Ha Noi to Nam Dinh', 'Demo', '96', '200', '2020-12-12', '2020-12-05', 'latest');
INSERT INTO public.plan(
	id, name, content, active_user, follow_user, start_day, end_day, status)
	VALUES ('8', 'HCM to Da Nang', 'Demo', '45', '145', '2020-12-12', '2020-12-05', 'latest');
INSERT INTO public.plan(
	id, name, content, active_user, follow_user, start_day, end_day, status)
	VALUES ('9', 'Ha Noi to Hoi An', 'Demo', '65', '120', '2020-12-12', '2020-12-05', 'latest');
INSERT INTO public.plan(
	id, name, content, active_user, follow_user, start_day, end_day, status)
	VALUES ('10', 'HCM to Phan Thiet', 'Demo', '85', '105', '2020-12-12', '2020-12-05', 'active');

