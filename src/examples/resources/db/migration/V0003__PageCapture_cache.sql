create table pagecapture_cache (
    id char(40) not null primary key,
    content blob not null,
    created_at timestamp not null default current_date
);