DROP TABLE IF EXISTS "user";

CREATE TABLE "user"
(
    user_id  SERIAL PRIMARY KEY,
    email TEXT not null UNIQUE,
    user_name TEXT not null,
    password  TEXT,
    created timestamp not null,
    changed timestamp,
    active boolean not null
);