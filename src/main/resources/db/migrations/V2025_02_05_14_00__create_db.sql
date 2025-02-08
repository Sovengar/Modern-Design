CREATE SCHEMA IF NOT EXISTS md; --AUTHORIZATION admin

CREATE TABLE MD.ACCOUNTS (
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    amount DECIMAL(19,2),
    currency VARCHAR(5),
    version INTEGER,
    created_by VARCHAR(255),
    created_on TIMESTAMP WITHOUT TIME ZONE,
    modified_by VARCHAR(255),
    modified_on TIMESTAMP WITHOUT TIME ZONE,
    deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE MD.USERS (
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
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
