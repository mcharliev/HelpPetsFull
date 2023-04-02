-- liquibase formatted sql

-- changeset mcharliev:2

CREATE TABLE detail_task
(
    id           SERIAL NOT NULL PRIMARY KEY,
    chat_id      BIGINT,
    name         TEXT,
    phoneNumber  TEXT
);