Drop TABLE IF EXISTS skill;
CREATE TABLE skill(
   ID       BIGINT auto_increment
       primary key,
   CATEGORY VARCHAR(20),
   NAME     VARCHAR(100) unique,
   SEQ      INTEGER
);