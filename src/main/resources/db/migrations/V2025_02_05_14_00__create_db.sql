create SCHEMA IF NOT EXISTS md; --AUTHORIZATION admin

create TABLE MD.ROLES (
    code VARCHAR(25) PRIMARY KEY,
    description VARCHAR(100)
);

create sequence MD.USERS_SQ start with 1;
create TABLE MD.USERS (
    user_id BIGINT PRIMARY KEY,
    user_uuid UUID UNIQUE,
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
    FOREIGN KEY (role_code) REFERENCES MD.ROLES(code)
);

create sequence MD.ACCOUNTS_SQ start with 1;
create TABLE MD.ACCOUNTS (
    account_id BIGINT PRIMARY KEY,
    account_number VARCHAR(255),
    balance DECIMAL(19,2),
    currency VARCHAR(5),
    address VARCHAR(255),
    date_of_last_transaction TIMESTAMP WITHOUT TIME ZONE,
    active BOOLEAN,
    user_uuid UUID,
    version INTEGER,
    created_by VARCHAR(255),
    created_on TIMESTAMP WITHOUT TIME ZONE,
    modified_by VARCHAR(255),
    modified_on TIMESTAMP WITHOUT TIME ZONE,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_uuid) REFERENCES MD.USERS(user_uuid)
);
