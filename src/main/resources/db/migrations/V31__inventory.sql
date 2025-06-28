create SCHEMA IF NOT EXISTS inventory;

create table inventory.stock (
    sku_id varchar(100) primary key,
    quantity_on_hand int not null,
    location varchar(100) not null
);

create sequence inventory.stock_reservations_sq start with 1;
create table inventory.stock_reservations (
    reservation_id BIGINT primary key,
    order_id UUID not null,
    product_id varchar(100) not null,
    quantity_on_hand int not null,
    created_at timestamp not null
);

create or replace view inventory.stock_view as
select stock.sku_id, stock.quantity_on_hand as stock
from inventory.stock;
