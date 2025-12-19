create SCHEMA IF NOT EXISTS banking;

create TABLE BANKING.ACCOUNT_HOLDERS (
    account_holder_id UUID PRIMARY KEY,
    name TEXT,
    personal_id_value TEXT,
    personal_id_type TEXT CHECK (personal_id_type IN ('DNI', 'NIE', 'PASSPORT')),
    country TEXT,
    birthdate DATE,
    phone_numbers TEXT,
    address JSONB,
    user_id UUID,
    version BIGINT,
    created_by TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    modified_by TEXT,
    modified_at TIMESTAMP WITHOUT TIME ZONE,
    deleted BOOLEAN DEFAULT FALSE
);
create index idx_ah_address_city on BANKING.ACCOUNT_HOLDERS ((address->>'city'));

create sequence BANKING.ACCOUNTS_SQ start with 1;
create TABLE BANKING.ACCOUNTS (
    account_id BIGINT PRIMARY KEY,
    account_number VARCHAR(48),
    balance DECIMAL(19,2),
    currency VARCHAR(5),
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

create materialized view banking.transactions_by_account_view as
select
    t.transaction_id,
    t.transaction_date,
    t.balance,
    t.currency,
    t.transaction_type,
    t.origin as origin_account,
    t.destination as destination_account
from banking.transactions t;

create unique index transactions_by_account_view_idx on banking.transactions_by_account_view (transaction_id);
create index transactions_by_account_view_origin_idx on banking.transactions_by_account_view (origin_account);
create index transactions_by_account_view_destination_idx on banking.transactions_by_account_view (destination_account);
