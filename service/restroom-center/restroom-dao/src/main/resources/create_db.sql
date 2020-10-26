use toilet_db;

create table toilet
(
    id        int auto_increment comment '主键'
        primary key,
    clean     tinyint default 1 not null,
    available tinyint default 1 not null
);

