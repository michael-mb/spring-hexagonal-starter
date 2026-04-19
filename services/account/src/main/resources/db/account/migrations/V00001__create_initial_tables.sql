--liquibase formatted sql

--changeset starter:create_account_tables
CREATE SCHEMA IF NOT EXISTS account_service;

DROP TABLE IF EXISTS account_service.ACCOUNT;
DROP TABLE IF EXISTS account_service.ADDRESS CASCADE;

CREATE TABLE account_service.ADDRESS
(
    id         BIGSERIAL    NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted    BOOLEAN      NOT NULL DEFAULT FALSE,
    country    VARCHAR(255) NOT NULL,
    state      VARCHAR(255),
    city       VARCHAR(500),
    street     VARCHAR(500),
    zip_code   VARCHAR(255),

    CONSTRAINT pk_address PRIMARY KEY (id)
);

CREATE TABLE account_service.ACCOUNT
(
    id           BIGSERIAL    NOT NULL,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted      BOOLEAN      NOT NULL DEFAULT FALSE,
    idp_user_id  VARCHAR(255) NOT NULL,
    first_name   VARCHAR(255),
    last_name    VARCHAR(255),
    email        VARCHAR(255),
    gender       VARCHAR(255),
    status       VARCHAR(50)  NOT NULL DEFAULT 'ACTIVE',
    phone_number VARCHAR(50),
    address_id   BIGINT,

    CONSTRAINT pk_account_id        PRIMARY KEY (id),
    CONSTRAINT uk_account_idp_user_id UNIQUE (idp_user_id),
    CONSTRAINT uk_account_email       UNIQUE (email),
    CONSTRAINT uk_account_phone_number UNIQUE (phone_number)
);

CREATE INDEX idx_account_email       ON account_service.ACCOUNT (email);
CREATE INDEX idx_account_idp_user_id ON account_service.ACCOUNT (idp_user_id);

--rollback DROP TABLE IF EXISTS account_service.ACCOUNT;
--rollback DROP TABLE IF EXISTS account_service.ADDRESS CASCADE;
