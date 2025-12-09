create sequence client_SEQ start with 1 increment by 1;
create sequence address_SEQ start with 1 increment by 1;
create sequence phone_SEQ start with 1 increment by 1;

create table client
(
    id   bigint not null primary key,
    name varchar(50),
    address_id bigint
);

create table address
(
    id     bigint not null primary key,
    street varchar(255) not null
);

create table phone
(
    id        bigint not null primary key,
    number    varchar(50) not null,
    client_id bigint not null
);

alter table client
    add constraint fk_client_address
        foreign key (address_id) references address(id);

alter table phone
    add constraint fk_phone_client
        foreign key (client_id) references client(id);

insert into address (id, street) values (nextval('address_SEQ'), 'New Street 1');
insert into address (id, street) values (nextval('address_SEQ'), 'New Street 2');
insert into address (id, street) values (nextval('address_SEQ'), 'New Street 3');

insert into client (id, name, address_id) values (nextval('client_SEQ'), 'Vladimir',1);
insert into client (id, name, address_id) values (nextval('client_SEQ'), 'Vasily',2);
insert into client (id, name, address_id) values (nextval('client_SEQ'), 'Ivan',3);

insert into phone (id, number, client_id) values (nextval('phone_SEQ'), '11-22-33',1);
insert into phone (id, number, client_id) values (nextval('phone_SEQ'), '22-33-44',2);
insert into phone (id, number, client_id) values (nextval('phone_SEQ'), '55-66-77',3);
insert into phone (id, number, client_id) values (nextval('phone_SEQ'), '88-99-00',3);