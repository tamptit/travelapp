-- create user table
CREATE TABLE public.plan_interactor
  (
      id bigserial  primary key,
      plan_id INT REFERENCES plan (id),
      user_id INT REFERENCES users (id),
      join_date timestamp DEFAULT NULL,
      status INT DEFAULT NULL
  );

INSERT INTO public.plan_interactor( plan_id, user_id, join_date, status) VALUES ( '10', '1', '2020-12-12',  '1');
INSERT INTO public.plan_interactor( plan_id, user_id, join_date, status) VALUES ( '9', '1', '2020-02-10',  '1');
INSERT INTO public.plan_interactor( plan_id, user_id, join_date, status) VALUES ( '8', '1', '2020-02-10',  '1');
INSERT INTO public.plan_interactor( plan_id, user_id, join_date, status) VALUES ( '7', '1', '2020-02-10',  '0');
INSERT INTO public.plan_interactor( plan_id, user_id, join_date, status) VALUES ( '6', '1', '2020-02-10',  '0');
INSERT INTO public.plan_interactor( plan_id, user_id, join_date, status) VALUES ( '5', '1', '2020-02-10',  '0');

ALTER TABLE public.plan DROP COLUMN count_user;
ALTER TABLE public.plan ADD COLUMN cover_plan varchar(255) default null;


ALTER TABLE public.users
    ADD COLUMN img_cover varchar(250) default null,
    ADD COLUMN img_avatar varchar(250) default null;
