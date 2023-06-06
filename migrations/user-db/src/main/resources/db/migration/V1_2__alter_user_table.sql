ALTER TABLE users
    DROP COLUMN created_at;
ALTER TABLE users
    DROP COLUMN deleted_at;
ALTER TABLE users
    DROP COLUMN updated_at;

ALTER TABLE users
    ADD COLUMN updated_at BIGINT;
ALTER TABLE users
    ADD COLUMN deleted_at BIGINT;
ALTER TABLE users
    ADD COLUMN created_at BIGINT;
