insert into MD.USERS (user_id, user_uuid, realname, username, email, internal_enterprise_email, password, country, status, phone_numbers, role_code, version) values
(nextval('MD.USERS_SQ'), 'f5e4e2a8-7f07-4b2e-9b7c-6c6b1fcd3c42', 'John Doe', 'johndoe', 'V4u0A@example.com', 'emailInBadState@ggg.com', 'password', 'ES', 'ACTIVE', '+34123456789;+34123456790', 'ADM', 0);

insert into MD.ACCOUNTS (account_id, account_number, balance, currency, address, date_of_last_transaction, active, user_uuid, version) values
(nextval('MD.ACCOUNTS_SQ'), 'f5e4e2a8-7f07-4b2e-9b7c-6c6b1fcd3c42', 1000, 'EUR', 'My house', '2022-01-01 00:00:00', true, 'f5e4e2a8-7f07-4b2e-9b7c-6c6b1fcd3c42', 0);
