-- create user table
CREATE TABLE public.plan
  (
      id bigserial  primary key,
      name       	varchar(45) DEFAULT NULL,
      content     text,
      active_user INT     DEFAULT NULL,
      follow_user INT     DEFAULT NULL,
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
	id bigserial  primary key,
	content text,
	status  varchar(45) DEFAULT NULL,
	time    date        DEFAULT NULL,
	user_id INT REFERENCES users(id),
	plan_id INT REFERENCES plan(id)
);

INSERT INTO public.plan(
	id, name, content, active_user, follow_user, start_day, end_day, status)
	VALUES ('11', 'Hà Nội đến Hồ Chí Minh', 'Demo', '65', '150', '2020-12-12', '2020-12-05', 'active');
INSERT INTO public.plan(
	id, name, content, active_user, follow_user, start_day, end_day, status)
	VALUES ('2', 'Hà Nội đến Lào Cai', 'Demo', '65', '200', '2020-12-12', '2020-12-05', 'latest');
INSERT INTO public.plan(
	id, name, content, active_user, follow_user, start_day, end_day, status)
	VALUES ('3', 'Hà Nội đến Hải Dương', 'Demo', '50', '300', '2020-12-12', '2020-12-05', 'latest');
INSERT INTO public.plan(
	id, name, content, active_user, follow_user, start_day, end_day, status)
	VALUES ('4', 'Hà Nội đến Thái Bình', 'Demo', '100', '150', '2020-12-12', '2020-12-05', 'latest');
INSERT INTO public.plan(
	id, name, content, active_user, follow_user, start_day, end_day, status)
	VALUES ('5', 'Hà Nội đến Cao Bằng', 'Demo', '80', '150', '2020-12-12', '2020-12-05', 'latest');
INSERT INTO public.plan(
	id, name, content, active_user, follow_user, start_day, end_day, status)
	VALUES ('6', 'Hà Nội đến Nam Định', 'Demo', '69', '200', '2020-12-12', '2020-12-05', 'latest');
INSERT INTO public.plan(
	id, name, content, active_user, follow_user, start_day, end_day, status)
	VALUES ('7', 'Hà Nội đến Quảng Ninh', 'Demo', '96', '200', '2020-12-12', '2020-12-05', 'latest');
INSERT INTO public.plan(
	id, name, content, active_user, follow_user, start_day, end_day, status)
	VALUES ('8', 'Hồ Chí Minh đến Đà Nẵng', 'Demo', '45', '145', '2020-12-12', '2020-12-05', 'latest');
INSERT INTO public.plan(
	id, name, content, active_user, follow_user, start_day, end_day, status)
	VALUES ('9', 'Hồ Chí Minh đến Hà Nội', 'Demo', '65', '120', '2020-12-12', '2020-12-05', 'latest');
INSERT INTO public.plan(
	id, name, content, active_user, follow_user, start_day, end_day, status)
	VALUES ('10', 'Hồ Chí Minh đến Hải Phòng', 'Demo', '85', '105', '2020-12-12', '2020-12-05', 'active');