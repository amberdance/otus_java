create table address (
   id bigserial not null,
    street varchar(255) not null,
    client_id bigint,
    primary key (id)
);

create table phone (
   id bigserial not null,
    number varchar(255) not null,
    client_id bigint,
    primary key (id)
);

alter table if exists phone
    add constraint uk_number unique (number);

alter table if exists address
    add constraint fk_client
    foreign key (client_id)
    references client;

alter table if exists phone
    add constraint fk_client
    foreign key (client_id)
    references client;