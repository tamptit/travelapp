-- udate user table
ALTER TABLE public.users
 ADD COLUMN provider_id varchar(100) default null;

ALTER TABLE public.users ALTER COLUMN provider set default 'local';

UPDATE public.users
SET provider = 'local';
