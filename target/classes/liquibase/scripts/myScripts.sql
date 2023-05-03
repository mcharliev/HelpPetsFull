-- liquibase formatted sql

-- changeset mcharliev:1

CREATE TABLE if not exists dog_shelter_users
(
    id           SERIAL NOT NULL PRIMARY KEY,
    name         TEXT,
    phone_Number TEXT
);
CREATE TABLE if not exists cat_shelter_users
(
    id           SERIAL NOT NULL PRIMARY KEY,
    name         TEXT,
    phone_Number TEXT
);

CREATE TABLE if not exists user_context
(
    chat_id     BIGINT PRIMARY KEY,
    cat_shelter BOOLEAN,
    dog_shelter BOOLEAN
);

CREATE TABLE if not exists owners
(
    id                  SERIAL NOT NULL PRIMARY KEY,
    chat_id             BIGINT,
    name                TEXT,
    start_probation     timestamp,
    end_probation       timestamp,
    probationary_status TEXT,
    period_extend       INTEGER
);
CREATE TABLE if not exists dogs
(
    id         SERIAL NOT NULL PRIMARY KEY,
    birth_date DATE,
    name       TEXT,
    breed      TEXT,
    owners_id  INTEGER
);
CREATE TABLE if not exists cats
(
    id         SERIAL NOT NULL PRIMARY KEY,
    birth_date DATE,
    name       TEXT,
    breed      TEXT,
    owners_id  INTEGER
);

CREATE TABLE if not exists reports
(
    id            SERIAL NOT NULL PRIMARY KEY,
    photo_report  BYTEA,
    string_report TEXT,
    last_report   timestamp,
    owners_id     INTEGER
)
