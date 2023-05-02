create table client (
   id bigint not null,
    name varchar(255),
    address_id bigint,
    primary key (id)
);

create table phone (
   id bigserial not null,
    number varchar(255),
    client_id bigint,
    primary key (id)
);

create table address (
   id bigserial not null,
    street varchar(255),
    primary key (id)
);

create sequence client_SEQ start with 1 increment by 1;

alter table if exists phone
   add constraint client_id_foreign
   foreign key (client_id)
   references client;

alter table if exists client
   add constraint address_id_foreign
   foreign key (address_id)
   references address;