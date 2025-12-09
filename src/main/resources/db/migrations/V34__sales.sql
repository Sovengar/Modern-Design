create SCHEMA IF NOT EXISTS sales;

create TABLE sales.shopping_carts (
    shopping_cart_id UUID PRIMARY KEY,
    customer_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

create TABLE sales.shopping_cart_item (
    shopping_cart_item_id UUID PRIMARY KEY,
    shopping_cart_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity INT NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    FOREIGN KEY (shopping_cart_id) REFERENCES sales.shopping_carts(shopping_cart_id)
);
