create SCHEMA IF NOT EXISTS md;

create TABLE md.event_publication (
    id UUID PRIMARY KEY,
    completion_date timestamp,
    event_type VARCHAR(255),
    listener_id VARCHAR(255),
    publication_date timestamp,
    serialized_event VARCHAR(255)
);

create TABLE md.deleted_rows (
    id SERIAL PRIMARY KEY,
    origin_table TEXT NOT NULL,
    origin_id TEXT NOT NULL,
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    deleted_by TEXT,
    reason TEXT,
    data JSONB NOT NULL
);

create index idx_deleted_rows_origin_table on md.deleted_rows(origin_table);
create index idx_deleted_rows_origin_id on md.deleted_rows(origin_id);
create index idx_deleted_rows_deleted_at on md.deleted_rows(deleted_at);
