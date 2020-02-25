-- creat password
create table public.password_token(
    id bigserial primary key,
    date_expiry date default null,
    token varchar(50) default null,
    user_id INT REFERENCES users (id)

);


