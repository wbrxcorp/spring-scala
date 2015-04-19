create table wikipedia_cache (
    page_name varchar(128) not null primary key,
    title text not null,
    content text,
    canonical_url varchar(256),
    last_modified timestamp not null,
    fetched_at timestamp not null default current_date
);

create table wikipedia_clients (
    ip_address varchar(64) not null primary key,
    fetched_at timestamp not null default current_date
);
