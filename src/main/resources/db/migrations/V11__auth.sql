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


