DROP TABLE IF EXISTS "user";
DROP TABLE IF EXISTS "refresh_token";

CREATE TABLE "user"
(
    user_id   SERIAL PRIMARY KEY,
    email     TEXT      not null UNIQUE,
    user_name TEXT      not null,
    password  TEXT,
    created   timestamp not null,
    changed   timestamp,
    active    boolean   not null
);


CREATE TABLE "refresh_token"
(
    token_id SERIAL PRIMARY KEY,
    user_id  int,
    token    TEXT      not null,
    created  timestamp not null,
    expired  timestamp not null,
    revoked  boolean   not null,

    CONSTRAINT fk_user_id
        FOREIGN KEY (user_id)
            REFERENCES "user" (user_id)
);