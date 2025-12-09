create SCHEMA IF NOT EXISTS shipping;

create TABLE shipping_address (
    id UUID PRIMARY KEY,
    street VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

create TABLE shipping_order (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL,
    address_id UUID NOT NULL,
    userStatus VARCHAR(100) NOT NULL,
    tracking_number VARCHAR(100),
    estimated_delivery_date DATE,
    carrier VARCHAR(100),
    shipping_cost DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (address_id) REFERENCES shipping_address(id)
);
