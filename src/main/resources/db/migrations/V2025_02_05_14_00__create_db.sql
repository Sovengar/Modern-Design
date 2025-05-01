create SCHEMA IF NOT EXISTS md; --AUTHORIZATION admin

create TABLE MD.ROLES (
    role_code VARCHAR(25) PRIMARY KEY,
    description VARCHAR(100)
);

create TABLE MD.USERS (
    user_id UUID PRIMARY KEY,
    realname VARCHAR(255),
    username VARCHAR(255),
    email VARCHAR(255),
    internal_enterprise_email VARCHAR(255),
    password VARCHAR(255),
    status VARCHAR(255),
    country VARCHAR(255),
    phone_numbers VARCHAR(500),
    role_code VARCHAR(25),
    version INTEGER,
    created_by VARCHAR(255),
    created_on TIMESTAMP WITHOUT TIME ZONE,
    modified_by VARCHAR(255),
    modified_on TIMESTAMP WITHOUT TIME ZONE,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (role_code) REFERENCES MD.ROLES(role_code)
);

create sequence MD.ACCOUNTS_SQ start with 1;
create TABLE MD.ACCOUNTS (
    account_id BIGINT PRIMARY KEY,
    account_number VARCHAR(48),
    balance DECIMAL(19,2),
    currency VARCHAR(5),
    address TEXT,
    date_of_last_transaction TIMESTAMP WITHOUT TIME ZONE,
    active BOOLEAN,
    user_id UUID,
    version INTEGER,
    created_by TEXT,
    created_on TIMESTAMP WITHOUT TIME ZONE,
    modified_by TEXT,
    modified_on TIMESTAMP WITHOUT TIME ZONE,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES MD.USERS(user_id)
);

create table MD.TRANSACTIONS (
    transaction_id VARCHAR(150) PRIMARY KEY,
    origin VARCHAR(255),
    destination VARCHAR(255),
    balance DECIMAL(19,2),
    currency VARCHAR(5),
    transaction_type VARCHAR(100),
    transaction_date TIMESTAMP WITHOUT TIME ZONE
);
