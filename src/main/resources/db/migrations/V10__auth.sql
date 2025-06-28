create SCHEMA IF NOT EXISTS auth;

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
    version BIGINT,
    created_by TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    modified_by TEXT,
    modified_at TIMESTAMP WITHOUT TIME ZONE,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (role_code) REFERENCES AUTH.ROLES(role_code)
);

insert into AUTH.ROLES (role_code, description) values
('ADM', 'Administrator'),
('TEC', 'Technician'),
('USER', 'User');

insert into AUTH.USERS (user_id, username, email, internal_enterprise_email, password, status, role_code, version, created_at, created_by) values
('f5e4e2a8-7f07-4b2e-9b7c-6c6b1fcd3c42', 'userJD', 'V4u0A@example.com', 'emailInBadState@ggg.com', 'encryptedPassword', 'ACTIVE', 'ADM', 0, '2025-02-05 14:00:00', 'SYSTEM'),
('56fa9d40-19ee-4c30-9438-33d364bafef9', 'user2', '13123121@example.com', 'abc@gmail.com', 'encryptedPassword', 'ACTIVE', 'TEC', 0, '2025-02-05 14:00:00', 'SYSTEM'),
('3a263b30-d80d-42d4-b64a-2243d258467f', 'user3', '4345534@example.com', 'emailInBadState@ggg.com', 'encryptedPassword', 'ACTIVE', 'TEC', 0, '2025-02-05 14:00:00', 'SYSTEM');

