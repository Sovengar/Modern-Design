create SCHEMA IF NOT EXISTS catalog;

create sequence catalog.products_SQ start with 1;
create TABLE catalog.products(
    sku_id varchar(100) primary key,
    name varchar(255),
    description varchar(255),
    price DOUBLE PRECISION,
    stars DOUBLE PRECISION
);

create sequence catalog.product_reviews_SQ start with 1;
create table catalog.product_reviews(
    product_review_id BIGINT primary key,
    title varchar(255),
    contents varchar(255),
    stars DOUBLE PRECISION,
    created_at timestamp without time zone,
    product_id varchar(100) not null,
    FOREIGN KEY (product_id) REFERENCES catalog.products(sku_id)
);

insert into catalog.products values('123', 'iPhone', 'Hipster Phone', '1000', '2.5');
