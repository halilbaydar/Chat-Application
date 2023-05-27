CREATE TABLE IF NOT EXISTS users (
                    id BIGSERIAL CONSTRAINT id PRIMARY KEY,
                    username VARCHAR(255) UNIQUE NOT NULL,
                    name VARCHAR(255) NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    status VARCHAR(255) NOT NULL,
                    role VARCHAR(255) NOT NULL,
                    created_at DATE NOT NULL DEFAULT CURRENT_DATE,
                    deleted_at DATE NOT NULL DEFAULT CURRENT_DATE,
                    updated_at DATE DEFAULT NULL
);
