CREATE TABLE public.schedule
(
    id bigserial  primary key,
    plan_id int references plan(id),
    activity 	varchar(255) DEFAULT NULL,
    vehicle 		varchar(100) DEFAULT NULL,
    start_point 	varchar(45) DEFAULT NULL,
    end_point 	varchar(45) DEFAULT NULL,
    date_start 		timestamp DEFAULT NULL,
    date_finish 	timestamp DEFAULT NULL
);

ALTER TABLE public.plan ADD COLUMN num_people int;



