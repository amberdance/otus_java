package ru.otus.data.crm.service;


import ru.otus.data.crm.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    List<Client> findAll();

    Optional<Client> findById(long id);

    Client findByUsername(String username);

    Client saveClient(Client client);

    boolean authenticate(String username, String password);

    Client createAdmin(String username, String password);
}
