DROP TABLE IF EXISTS public.plan CASCADE;

CREATE TABLE public.plan
(
	id bigserial  primary key,
	name       	varchar(45) DEFAULT NULL,
	content     text,
	start_day   date        DEFAULT NULL,
	end_day     date        DEFAULT NULL,
	status      varchar(45) DEFAULT NULL,
	created_day timestamp  DEFAULT NULL,
	user_id int references users(id),
	image      text DEFAULT NULL,
	count_user int
);
INSERT INTO public.plan(
	 name, content, start_day, end_day, status,created_day,user_id,image,count_user)
	VALUES ( 'Hồ Chí Minh đến Hà Nội', 'Demo', '2020-02-12', '2020-12-05', 'active','2020-01-16 16:38:13','1','https://r-cf.bstatic.com/images/hotel/max1024x768/123/123568450.jpg','20');
	INSERT INTO public.plan(
	 name, content, start_day, end_day, status,created_day,user_id,image,count_user)
	VALUES ( 'Hồ Chí Minh đến Hà Nội', 'Demo', '2020-02-13', '2020-12-05', 'active','2020-02-20 16:02:13','1','https://r-cf.bstatic.com/images/hotel/max1024x768/123/123568450.jpg','19');
	INSERT INTO public.plan(
	 name, content, start_day, end_day, status,created_day,user_id,image,count_user)
	VALUES ( 'Hồ Chí Minh đến Hà Nội', 'Demo 2', '2020-02-11', '2020-12-05', 'active','2020-02-20 16:02:13','1','https://r-cf.bstatic.com/images/hotel/max1024x768/123/123568450.jpg','25');
	INSERT INTO public.plan(
	 name, content, start_day, end_day, status,created_day,user_id,image,count_user)
	VALUES ( 'Thanh Hóa đến Sài Gòn', 'Demo 3', '2020-02-14', '2020-12-05', 'active','2020-02-25 16:02:13','2','https://r-cf.bstatic.com/images/hotel/max1024x768/123/123568450.jpg','40');
	INSERT INTO public.plan(
	 name, content, start_day, end_day, status,created_day,user_id,image,count_user)
	VALUES ( 'Thanh Hóa đến Sài Gòn', 'Demo 5', '2020-02-14', '2020-12-05', 'active','2020-02-25 16:02:13','2','https://r-cf.bstatic.com/images/hotel/max1024x768/123/123568450.jpg','35');
	INSERT INTO public.plan(
	 name, content, start_day, end_day, status,created_day,user_id,image,count_user)
	VALUES ( 'Thanh Hóa đến Sài Gòn', 'Healjdadh', '2020-02-13', '2020-12-05', 'active','2020-02-23 16:25:13','2','https://r-cf.bstatic.com/images/hotel/max1024x768/123/123568450.jpg','69');
	INSERT INTO public.plan(
	 name, content, start_day, end_day, status,created_day,user_id,image,count_user)
	VALUES ( 'Thanh Hóa đến Hà Nội', 'Demo', '2020-12-12', '2020-02-15', 'active','2020-02-23 16:25:13','3','https://r-cf.bstatic.com/images/hotel/max1024x768/123/123568450.jpg','96');
	INSERT INTO public.plan(
	 name, content, start_day, end_day, status,created_day,user_id,image,count_user)
	VALUES ( 'Thanh Hóa đến Hà Nội', 'Demo', '2020-12-12', '2020-02-16', 'active','2020-02-19 16:38:13','3','https://r-cf.bstatic.com/images/hotel/max1024x768/123/123568450.jpg','52');
	INSERT INTO public.plan(
 name, content, start_day, end_day, status,created_day,user_id,image,count_user)
	VALUES ( 'Thanh Hóa đến Hà Nội', 'Demo', '2020-12-12', '2020-12-05', 'active','2020-02-22 16:38:13','3','https://r-cf.bstatic.com/images/hotel/max1024x768/123/123568450.jpg','25');
	INSERT INTO public.plan(
	 name, content, start_day, end_day, status,created_day,user_id,image,count_user)
	VALUES ( 'Thanh Hóa đến Hà Nội', 'Demo', '2020-12-12', '2020-12-05', 'active','2020-01-24 16:38:13','4','https://r-cf.bstatic.com/images/hotel/max1024x768/123/123568450.jpg','19');
	INSERT INTO public.plan(
	 name, content, start_day, end_day, status,created_day,user_id,image,count_user)
	VALUES ( 'Hà Nội đến Nghệ An', 'Demo', '2020-12-12', '2020-12-05', 'active','2020-01-23 16:38:13','4','https://r-cf.bstatic.com/images/hotel/max1024x768/123/123568450.jpg','50');
	INSERT INTO public.plan(
	 name, content, start_day, end_day, status,created_day,user_id,image,count_user)
	VALUES ( 'Hà Nội đến Nghệ An', 'Demo', '2020-12-12', '2020-12-05', 'active','2020-01-23 16:38:13','4','https://r-cf.bstatic.com/images/hotel/max1024x768/123/123568450.jpg','20');
	INSERT INTO public.plan(
	 name, content, start_day, end_day, status,created_day,user_id,image,count_user)
	VALUES ( 'Hà Nội đến Nghệ An', 'Demo', '2020-12-12', '2020-12-05', 'active','2020-01-21 16:38:13','4','https://r-cf.bstatic.com/images/hotel/max1024x768/123/123568450.jpg','20');
	INSERT INTO public.plan(
	 name, content, start_day, end_day, status,created_day,user_id,image,count_user)
	VALUES ( 'Hà Nội đến Nghệ An', 'Demo', '2020-12-12', '2020-12-05', 'active','2020-01-20 16:38:13','4','https://r-cf.bstatic.com/images/hotel/max1024x768/123/123568450.jpg','20');
	INSERT INTO public.plan(
	 name, content, start_day, end_day, status,created_day,user_id,image,count_user)
	VALUES ( 'Hồ Chí Minh đến Nghệ An', 'Demo', '2020-12-12', '2020-12-05', 'active','2020-01-19 16:38:13','4','https://r-cf.bstatic.com/images/hotel/max1024x768/123/123568450.jpg','20');
