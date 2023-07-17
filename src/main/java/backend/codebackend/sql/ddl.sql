create table member
(
    id bigint generated by default as identity,
    Login varchar(255),
    pw varchar(255),
    pwcheck varchar(255),
    nickname char(20),
    pnum char(10),
    certified char(7),
    primary key (id)
);

ALTER TABLE member ALTER COLUMN id RESTART WITH 1 !-- identity 1로 초기화 0으로는 안됨 오류남 --!