ALTER TABLE public.plan drop column status
ALTER TABLE public.plan add status int default 1;

