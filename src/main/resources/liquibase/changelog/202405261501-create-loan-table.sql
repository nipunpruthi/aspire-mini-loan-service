--liquibase formatted sql
--changeset Nipun Pruthi:202405261501

CREATE TABLE IF NOT EXISTS loan (
       id               UUID                        PRIMARY KEY,
       customer_id      VARCHAR                        NOT NULL,
       amount           decimal                     NOT NULL,
       loan_term        INT                     NOT NULL,
       pending_amount   decimal                  NOT NULL,
       pending_term     INT                     NOT NULL,
       status           VARCHAR                 NOT NULL,
       date_applied     DATE                    NOT NULL,
       date_approved    DATE                    NULL,
       approved_by      VARCHAR                 NULL
);