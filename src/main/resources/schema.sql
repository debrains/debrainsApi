Drop TABLE IF EXISTS skill;
CREATE TABLE skill(
   ID       BIGINT auto_increment
       primary key,
   CATEGORY VARCHAR(255),
   NAME     VARCHAR(255) unique,
   SEQ      INTEGER
);