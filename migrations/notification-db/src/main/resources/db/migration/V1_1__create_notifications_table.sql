CREATE TABLE IF NOT EXISTS notifications (
                                             id INT PRIMARY KEY,
                                             user_id INT NOT NULL,
                                             external_notification_id VARCHAR(255) UNIQUE NOT NULL,
    provider VARCHAR(255) NOT NULL,
    type INT NOT NULL,
    created_at DATE NOT NULL DEFAULT CURRENT_DATE,
    updated_at DATE NOT NULL DEFAULT CURRENT_DATE,
    deleted_at DATE
    );

CREATE TABLE IF NOT EXISTS notification_log (
                                                id INT PRIMARY KEY,
                                                notification_id INT NOT NULL CONSTRAINT notification_foreign_key_for_log REFERENCES notifications (id),
    data JSON DEFAULT '{}',
    result INT DEFAULT 0,
    created_at DATE NOT NULL DEFAULT CURRENT_DATE,
    updated_at DATE NOT NULL DEFAULT CURRENT_DATE,
    deleted_at DATE
    );

CREATE TABLE IF NOT EXISTS notification_settings (
                                                     id INT PRIMARY KEY,
                                                     user_id INT NOT NULL,
                                                     notification_id INT UNIQUE NOT NULL CONSTRAINT notification_foreign_key_for_settings REFERENCES notifications (id),
    settings JSON DEFAULT '{}',
    created_at DATE NOT NULL DEFAULT CURRENT_DATE,
    updated_at DATE NOT NULL DEFAULT CURRENT_DATE,
    deleted_at DATE
    );
