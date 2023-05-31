insert into clients(name, password, username)
values ('SomeClient', 'notsecure', 'admin')
returning id;

insert into addresses(address, client_id)
values ('SomeAddressHere', lastval());

insert into phones(number, client_id)
values ('+7666666666', lastval());
