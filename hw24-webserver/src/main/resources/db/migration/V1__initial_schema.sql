create table addresses
(
    id    bigserial not null primary key,
    label varchar(64)
);

create table clients
(
    id         bigserial not null primary key,
    name       varchar(64),
    username   varchar(64),
    password   varchar(256),
    address_id bigint,
    foreign key (address_id) references addresses
);

create table phones
(
    id        bigserial not null primary key,
    number    varchar(12),
    client_id bigint,
    foreign key (client_id) references clients
)
