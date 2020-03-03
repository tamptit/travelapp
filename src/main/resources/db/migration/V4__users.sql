-- udate user table
ALTER TABLE public.users
    ADD COLUMN join_date date default null

INSERT INTO public.users(username, full_name, email, password, date_birth, join_date ,gender)
VALUES ('lenhatnam001', 'LeNhatNam', 'namnhat001@gamil', '$2y$11$8nM7sXQEjLmZTTsynYkyz.15CJ3G/y/lIWlr0CTci6fFB4TBMyqzu
','1999-09-08' ,'2020-02-28', true);
INSERT INTO public.users(username, full_name, email, password, date_birth, join_date ,gender)
VALUES ('lenhatnam002', 'LeNhatNam', 'namnhat002@gamil', '$2y$11$8nM7sXQEjLmZTTsynYkyz.15CJ3G/y/lIWlr0CTci6fFB4TBMyqzu
','1999-08-09' ,'2020-02-28', true);
INSERT INTO public.users(username, full_name, email, password, date_birth, join_date ,gender)
VALUES ('lenhatnam003', 'LeNhatNam', 'namnhat003@gamil', '$2y$11$8nM7sXQEjLmZTTsynYkyz.15CJ3G/y/lIWlr0CTci6fFB4TBMyqzu
','1999-10-09' ,'2020-02-29', true);
INSERT INTO public.users(username, full_name, email, password, date_birth, join_date ,gender)
VALUES ('lenhatnam004', 'LeNhatNam', 'namnhat004@gamil', '$2y$11$8nM7sXQEjLmZTTsynYkyz.15CJ3G/y/lIWlr0CTci6fFB4TBMyqzu
','1999-10-09' ,'2020-02-29', true);
INSERT INTO public.users(username, full_name, email, password, date_birth, join_date ,gender)
VALUES ('lenhatnam005', 'LeNhatNam', 'namnhat005@gamil', '$2y$11$8nM7sXQEjLmZTTsynYkyz.15CJ3G/y/lIWlr0CTci6fFB4TBMyqzu
','1999-10-09' ,'2020-03-01', true);
INSERT INTO public.users(username, full_name, email, password, date_birth, join_date ,gender)
VALUES ('lenhatnam006', 'LeNhatNam', 'namnhat006@gamil', '$2y$11$8nM7sXQEjLmZTTsynYkyz.15CJ3G/y/lIWlr0CTci6fFB4TBMyqzu
','2001-10-01' ,'2020-03-01', true);
INSERT INTO public.users(username, full_name, email, password, date_birth, join_date ,gender)
VALUES ('lenhatnam007', 'LeNhatNam', 'namnhat007@gamil', '$2y$11$8nM7sXQEjLmZTTsynYkyz.15CJ3G/y/lIWlr0CTci6fFB4TBMyqzu
','2000-09-05' ,'2020-03-01', true);
INSERT INTO public.users(username, full_name, email, password, date_birth, join_date ,gender)
VALUES ('lenhatnam008', 'LeNhatNam', 'namnhat008@gamil', '$2y$11$8nM7sXQEjLmZTTsynYkyz.15CJ3G/y/lIWlr0CTci6fFB4TBMyqzu
','2000-09-04' ,'2020-03-02', true);
INSERT INTO public.users(username, full_name, email, password, date_birth, join_date ,gender)
VALUES ('lenhatnam009', 'LeNhatNam', 'namnhat009@gamil', '$2y$11$8nM7sXQEjLmZTTsynYkyz.15CJ3G/y/lIWlr0CTci6fFB4TBMyqzu
','2000-09-03' ,'2020-03-02', true);
INSERT INTO public.users(username, full_name, email, password, date_birth, join_date ,gender)
VALUES ('lenhatnam010', 'LeNhatNam', 'namnhat010@gamil', '$2y$11$8nM7sXQEjLmZTTsynYkyz.15CJ3G/y/lIWlr0CTci6fFB4TBMyqzu
','2000-09-02' ,'2020-03-02', true);

