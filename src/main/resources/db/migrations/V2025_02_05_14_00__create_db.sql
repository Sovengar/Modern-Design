create SCHEMA IF NOT EXISTS banking;
create SCHEMA IF NOT EXISTS auth;
create SCHEMA IF NOT EXISTS md;

create TABLE MD.event_publication (
    id UUID PRIMARY KEY,
    completion_date timestamp,
    event_type VARCHAR(255),
    listener_id VARCHAR(255),
    publication_date timestamp,
    serialized_event VARCHAR(255)
);

create TABLE AUTH.ROLES (
    role_code TEXT PRIMARY KEY,
    description TEXT
);

create TABLE AUTH.USERS (
    user_id UUID PRIMARY KEY,
    username TEXT,
    email TEXT,
    internal_enterprise_email TEXT,
    password VARCHAR(70),
    status TEXT,
    role_code TEXT,
    version INTEGER,
    created_by TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    modified_by TEXT,
    modified_at TIMESTAMP WITHOUT TIME ZONE,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (role_code) REFERENCES AUTH.ROLES(role_code)
);

create TABLE BANKING.ACCOUNT_HOLDERS (
    account_holder_id UUID PRIMARY KEY,
    name TEXT,
    personal_id_value TEXT,
    personal_id_type TEXT CHECK (personal_id_type IN ('DNI', 'NIE', 'PASSPORT')),
    country TEXT,
    birthdate DATE,
    phone_numbers TEXT,
    user_id UUID,
    version BIGINT,
    created_by TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    modified_by TEXT,
    modified_at TIMESTAMP WITHOUT TIME ZONE,
    deleted BOOLEAN DEFAULT FALSE
);

create sequence BANKING.ACCOUNTS_SQ start with 1;
create TABLE BANKING.ACCOUNTS (
    account_id BIGINT PRIMARY KEY,
    account_number VARCHAR(48),
    balance DECIMAL(19,2),
    currency VARCHAR(5),
    address TEXT,
    status TEXT,
    account_holder_id UUID,
    version BIGINT,
    created_by TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    modified_by TEXT,
    modified_at TIMESTAMP WITHOUT TIME ZONE,
    FOREIGN KEY (account_holder_id) REFERENCES BANKING.ACCOUNT_HOLDERS(account_holder_id)
);

create TABLE BANKING.TRANSACTIONS (
    transaction_id VARCHAR(150) PRIMARY KEY,
    origin TEXT,
    destination TEXT,
    balance DECIMAL(19,2),
    currency VARCHAR(5),
    transaction_type TEXT,
    transaction_date TIMESTAMP WITHOUT TIME ZONE
);

create TABLE MD.deleted_rows (
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
