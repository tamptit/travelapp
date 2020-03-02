-- create user table
CREATE TABLE public.plan_interactor
  (
      id bigserial  primary key,
      plan_id INT REFERENCES plan (id),
      user_id INT REFERENCES users (id),
      join_date timestamp DEFAULT NULL,
      status INT DEFAULT NULL
  );

