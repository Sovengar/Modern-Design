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




insert into BANKING.ACCOUNT_HOLDERS (account_holder_id, name, personal_id_value, personal_id_type, country, birthdate, phone_numbers, address, user_id, version, created_at, created_by) values
('f5e4e2a8-7f07-4b2e-9b7c-6c6b1fcd3c42', 'John Doe', '48732228A', 'DNI', 'ES', '1990-02-05', '+34123456789;+34123456790', '{ "street": "Rupert", "city": "Orihuela", "postalCode": "08001", "state": "Comunidad Valenciana", "country": "Spain"}', 'f5e4e2a8-7f07-4b2e-9b7c-6c6b1fcd3c42', 0, '2025-02-05 14:00:00', 'SYSTEM'),
('56fa9d40-19ee-4c30-9438-33d364bafef9', 'Usuario2', 'X8732228A', 'NIE', 'ES', '1990-02-05', null, null, '56fa9d40-19ee-4c30-9438-33d364bafef9', 0, '2025-02-05 14:00:00', 'SYSTEM'),
('3a263b30-d80d-42d4-b64a-2243d258467f', 'Usuario3', '68732228A', 'DNI', 'ES', '1990-02-05', null, null, '3a263b30-d80d-42d4-b64a-2243d258467f', 0, '2025-02-05 14:00:00', 'SYSTEM');

insert into BANKING.ACCOUNTS (account_id, account_number, balance, currency, status, account_holder_id, version, created_at, created_by) values
(nextval('BANKING.ACCOUNTS_SQ'), 'f5e4e2a8-7f07-4b2e-9b7c-6c6b1fcd3c42', 1000, 'EUR', 'ACTIVE', 'f5e4e2a8-7f07-4b2e-9b7c-6c6b1fcd3c42', 0, '2025-02-05 14:00:00', 'SYSTEM'),
(nextval('BANKING.ACCOUNTS_SQ'), '56fa9d40-19ee-4c30-9438-33d364bafef9', 600, 'EUR', 'ACTIVE', '56fa9d40-19ee-4c30-9438-33d364bafef9', 0, '2025-02-05 14:00:00', 'SYSTEM'),
(nextval('BANKING.ACCOUNTS_SQ'), '3a263b30-d80d-42d4-b64a-2243d258467f', 100, 'EUR', 'INACTIVE', '3a263b30-d80d-42d4-b64a-2243d258467f', 0, '2025-02-05 14:00:00', 'SYSTEM');
