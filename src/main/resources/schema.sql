DROP TABLE IF EXISTS [user];

CREATE TABLE [user]
(
    user_id   BIGINT IDENTITY (1,1) PRIMARY KEY,
    user_name varchar(64) NOT NULL,
    password  varchar(max)
);