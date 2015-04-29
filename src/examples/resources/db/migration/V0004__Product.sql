create table products (
    id varchar(32) not null primary key,
    title varchar(256) not null,
    price int not null,
    available boolean not null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp
);

create table product_tags (
    product_id varchar(32) not null,
    tag varchar(32) not null,
    primary key(product_id, tag)
);

insert into products(id,title,price,available) values('B00S5V6EG0','ASUSTeK NVIDIA GeForce GTX980搭載 ROG MATRIX-GTX980-P-4GD5',95224,true);
insert into products(id,title,price,available) values('B00V7K2AFK','ASRock X99 WS-E/10G マザーボード Intel 10GNic採用 MB2368 X99 WS-E/10G',114480,false);

insert into product_tags(product_id,tag) values('B00S5V6EG0','ビデオカード');
insert into product_tags(product_id,tag) values('B00S5V6EG0','PCIe');
insert into product_tags(product_id,tag) values('B00S5V6EG0','nVidia');
insert into product_tags(product_id,tag) values('B00S5V6EG0','GTX980');
insert into product_tags(product_id,tag) values('B00V7K2AFK','マザーボード');
insert into product_tags(product_id,tag) values('B00V7K2AFK','X99');
insert into product_tags(product_id,tag) values('B00V7K2AFK','ASRock');
insert into product_tags(product_id,tag) values('B00V7K2AFK','DDR4');
insert into product_tags(product_id,tag) values('B00V7K2AFK','LGA2011');
