insert into AUTH.USERS (user_id, username, email, internal_enterprise_email, password, status, role_code, version, created_at, created_by) values
('f5e4e2a8-7f07-4b2e-9b7c-6c6b1fcd3c42', 'userJD', 'V4u0A@example.com', 'emailInBadState@ggg.com', 'encryptedPassword', 'ACTIVE', 'ADM', 0, '2025-02-05 14:00:00', 'SYSTEM'),
('56fa9d40-19ee-4c30-9438-33d364bafef9', 'user2', '13123121@example.com', 'abc@gmail.com', 'encryptedPassword', 'ACTIVE', 'TEC', 0, '2025-02-05 14:00:00', 'SYSTEM'),
('3a263b30-d80d-42d4-b64a-2243d258467f', 'user3', '4345534@example.com', 'emailInBadState@ggg.com', 'encryptedPassword', 'ACTIVE', 'TEC', 0, '2025-02-05 14:00:00', 'SYSTEM');

insert into BANKING.ACCOUNT_HOLDERS (account_holder_id, name, personal_id_value, personal_id_type, country, birthdate, phone_numbers, address, user_id, version, created_at, created_by) values
('f5e4e2a8-7f07-4b2e-9b7c-6c6b1fcd3c42', 'John Doe', '48732228A', 'DNI', 'ES', '1990-02-05', '+34123456789;+34123456790', '{ "street": "Calle Falsa 123", "city": "Barcelona", "postalCode": "08001", "state": "España"}', 'f5e4e2a8-7f07-4b2e-9b7c-6c6b1fcd3c42', 0, '2025-02-05 14:00:00', 'SYSTEM'),
('56fa9d40-19ee-4c30-9438-33d364bafef9', 'Usuario2', 'X8732228A', 'NIE', 'ES', '1990-02-05', null, null, '56fa9d40-19ee-4c30-9438-33d364bafef9', 0, '2025-02-05 14:00:00', 'SYSTEM'),
('3a263b30-d80d-42d4-b64a-2243d258467f', 'Usuario3', '68732228A', 'DNI', 'ES', '1990-02-05', null, null, '3a263b30-d80d-42d4-b64a-2243d258467f', 0, '2025-02-05 14:00:00', 'SYSTEM');

insert into BANKING.ACCOUNTS (account_id, account_number, balance, currency, status, account_holder_id, version, created_at, created_by) values
(nextval('BANKING.ACCOUNTS_SQ'), 'f5e4e2a8-7f07-4b2e-9b7c-6c6b1fcd3c42', 1000, 'EUR', 'ACTIVE', 'f5e4e2a8-7f07-4b2e-9b7c-6c6b1fcd3c42', 0, '2025-02-05 14:00:00', 'SYSTEM'),
(nextval('BANKING.ACCOUNTS_SQ'), '56fa9d40-19ee-4c30-9438-33d364bafef9', 600, 'EUR', 'ACTIVE', '56fa9d40-19ee-4c30-9438-33d364bafef9', 0, '2025-02-05 14:00:00', 'SYSTEM'),
(nextval('BANKING.ACCOUNTS_SQ'), '3a263b30-d80d-42d4-b64a-2243d258467f', 100, 'EUR', 'INACTIVE', '3a263b30-d80d-42d4-b64a-2243d258467f', 0, '2025-02-05 14:00:00', 'SYSTEM');
