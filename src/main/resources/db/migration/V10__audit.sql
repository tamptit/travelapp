ALTER TABLE public.plan ADD COLUMN created_at timestamp;
ALTER TABLE public.plan ADD COLUMN updated_at timestamp;

CREATE TABLE public.Schedule
(
    id bigserial  primary key,
    plan_id int references plan(id),
    activivity 	varchar(255) DEFAULT NULL,
    vehicle 		varchar(100) DEFAULT NULL,
    start_point 	varchar(45) DEFAULT NULL,
    end_point 	varchar(45) DEFAULT NULL,
    dateStart 		timestamp DEFAULT NULL,
    dateFinish 	timestamp DEFAULT NULL
);
