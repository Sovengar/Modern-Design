create SCHEMA IF NOT EXISTS search;

create TABLE SEARCH.account_with_user_info (
    user_id UUID PRIMARY KEY,
    account_number VARCHAR(48),
    balance DECIMAL(19,2),
    username TEXT,
    email TEXT
);
