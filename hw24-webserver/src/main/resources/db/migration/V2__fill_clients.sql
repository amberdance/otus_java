insert into addresses(label)
values ('SomeAddressHere')
returning id as test;

insert into clients(name, username, password, address_id)
values ('SomeClient', 'admin', 'not_secure', lastval())
returning id;

insert into phones(number, client_id)
values ('+7666666666', lastval());
