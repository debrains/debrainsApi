Drop TABLE IF EXISTS skill;
CREATE TABLE skill(
   ID       BIGINT auto_increment
       primary key,
   CATEGORY VARCHAR(20),
   NAME     VARCHAR(100) unique,
   SEQ      INTEGER
);

CREATE TABLE IF NOT EXISTS user_table
(
    user_id         bigint auto_increment
        primary key,
    email           varchar(255)  not null
);