CREATE SCHEMA IF NOT EXISTS md; --AUTHORIZATION admin

CREATE SEQUENCE MD.USERS_SQ START WITH 1;
CREATE TABLE MD.USERS (
    id BIGINT PRIMARY KEY,
    uuid UUID DEFAULT gen_random_uuid(),
    realname VARCHAR(255),
    username VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    country VARCHAR(255),
    version INTEGER,
    created_by VARCHAR(255),
    created_on TIMESTAMP WITHOUT TIME ZONE,
    modified_by VARCHAR(255),
    modified_on TIMESTAMP WITHOUT TIME ZONE,
    deleted BOOLEAN DEFAULT FALSE
);

CREATE SEQUENCE MD.ACCOUNTS_SQ START WITH 1;
CREATE TABLE MD.ACCOUNTS (
    id BIGINT PRIMARY KEY,
    account_number VARCHAR(255),
    balance DECIMAL(19,2),
    currency VARCHAR(5),
    address VARCHAR(255),
    date_of_last_transaction TIMESTAMP WITHOUT TIME ZONE,
    active BOOLEAN,
    user_id BIGINT,
    version INTEGER,
    created_by VARCHAR(255),
    created_on TIMESTAMP WITHOUT TIME ZONE,
    modified_by VARCHAR(255),
    modified_on TIMESTAMP WITHOUT TIME ZONE,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES MD.USERS(id)
);
