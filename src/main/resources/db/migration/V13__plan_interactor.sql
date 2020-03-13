ALTER TABLE public.plan_interactor
    DROP COLUMN status;
ALTER TABLE public.plan_interactor
    ADD COLUMN follow BOOLEAN default false;

ALTER TABLE public.plan_interactor
    ADD COLUMN joined BOOLEAN default false;