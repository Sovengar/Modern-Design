INSERT INTO MD.USERS (uuid, realname, username, email, password, country, version) VALUES
('f5e4e2a8-7f07-4b2e-9b7c-6c6b1fcd3c42', 'John Doe', 'johndoe', 'V4u0A@example.com', 'password', 'ES', 0);

INSERT INTO MD.ACCOUNTS (id, account_number, amount, currency, address, date_of_last_transaction, active, user_id, version) VALUES
(nextval('MD.ACCOUNTS_SQ'), 'f5e4e2a8-7f07-4b2e-9b7c-6c6b1fcd3c42', 1000, 'EUR', 'My house', '2022-01-01 00:00:00', TRUE, (select uuid from md.users where uuid = 'f5e4e2a8-7f07-4b2e-9b7c-6c6b1fcd3c42'), 0);
