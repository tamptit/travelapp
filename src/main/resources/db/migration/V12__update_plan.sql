ALTER TABLE public.plan
    DROP COLUMN status;

ALTER TABLE public.plan
    ADD COLUMN status int default null