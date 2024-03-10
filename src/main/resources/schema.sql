drop table if exists users cascade;

CREATE EXTENTION IF NOT EXISTS *uuid-ossp*;

CREATE TABLE IF NOT EXISTS users (
id                uuid         NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4 (),
name              VARCHAR(255) NOT NULL,
birth_date        timestamp    NOT NULL,
registration_date timestamp    NOT NULL
);