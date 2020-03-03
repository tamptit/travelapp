-- udate user table
ALTER TABLE public.users
 ADD COLUMN principal_id varchar(100) default null,
 ADD COLUMN login_type varchar(25) default null


