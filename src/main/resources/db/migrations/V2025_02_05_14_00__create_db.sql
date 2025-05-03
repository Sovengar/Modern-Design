create SCHEMA IF NOT EXISTS md; --AUTHORIZATION admin

create TABLE MD.ROLES (
    role_code TEXT PRIMARY KEY,
    description TEXT
);

create TABLE MD.USERS (
    user_id UUID PRIMARY KEY,
    realname TEXT,
    username TEXT,
    email TEXT,
    internal_enterprise_email TEXT,
    password VARCHAR(70),
    status TEXT,
    country TEXT,
    phone_numbers TEXT,
    role_code TEXT,
    version INTEGER,
    created_by TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    modified_by TEXT,
    modified_at TIMESTAMP WITHOUT TIME ZONE,
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
    status TEXT,
    user_id UUID,
    version INTEGER,
    created_by TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    modified_by TEXT,
    modified_at TIMESTAMP WITHOUT TIME ZONE,
    FOREIGN KEY (user_id) REFERENCES MD.USERS(user_id)
);

create TABLE MD.TRANSACTIONS (
    transaction_id VARCHAR(150) PRIMARY KEY,
    origin TEXT,
    destination TEXT,
    balance DECIMAL(19,2),
    currency VARCHAR(5),
    transaction_type TEXT,
    transaction_date TIMESTAMP WITHOUT TIME ZONE
);


create TABLE deleted_rows (
    id SERIAL PRIMARY KEY,
    origin_table TEXT NOT NULL,
    origin_id TEXT NOT NULL,
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    deleted_by TEXT,
    reason TEXT,
    data JSONB NOT NULL
);

create index idx_deleted_rows_origin_table on deleted_rows(origin_table);
create index idx_deleted_rows_origin_id on deleted_rows(origin_id);
create index idx_deleted_rows_deleted_at on deleted_rows(deleted_at);
