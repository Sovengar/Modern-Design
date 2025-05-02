insert into MD.USERS (user_id, realname, username, email, internal_enterprise_email, password, country, status, phone_numbers, role_code, version, created_at, created_by) values
('f5e4e2a8-7f07-4b2e-9b7c-6c6b1fcd3c42', 'John Doe', 'johndoe', 'V4u0A@example.com', 'emailInBadState@ggg.com', 'password', 'ES', 'ACTIVE', '+34123456789;+34123456790', 'ADM', 0, '2025-02-05 14:00:00', 'SYSTEM');

insert into MD.ACCOUNTS (account_id, account_number, balance, currency, address, status, user_id, version, created_at, created_by) values
(nextval('MD.ACCOUNTS_SQ'), 'f5e4e2a8-7f07-4b2e-9b7c-6c6b1fcd3c42', 1000, 'EUR', 'My house', 'ACTIVE', 'f5e4e2a8-7f07-4b2e-9b7c-6c6b1fcd3c42', 0, '2025-02-05 14:00:00', 'SYSTEM');
