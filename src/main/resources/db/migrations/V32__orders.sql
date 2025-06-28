create SCHEMA IF NOT EXISTS orders;

create table orders.orders (
    order_id UUID PRIMARY KEY,
    placed_on DATE,
    customer_id TEXT,
    shipping_address TEXT,
    shipping_tracking_number TEXT,
    status TEXT
);

create table orders.order_items (
    order_id uuid not null references orders.orders(order_id),
    items integer,
    items_key varchar(255) not null,
    primary key (order_id, items_key)
);
