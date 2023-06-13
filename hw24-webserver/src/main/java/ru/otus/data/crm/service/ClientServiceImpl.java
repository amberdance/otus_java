package ru.otus.data.crm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.data.core.repository.DataTemplate;
import ru.otus.data.core.sessionmanager.TransactionManager;
import ru.otus.data.crm.model.Address;
import ru.otus.data.crm.model.Client;
import ru.otus.data.crm.model.Phone;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final TransactionManager transactionManager;
    private final DataTemplate<Client> clientDataTemplate;


    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            if (client.getId() == null) {
                clientDataTemplate.insert(session, client);
                log.info("Created client: {}", client);

                return client;
            }
            clientDataTemplate.update(session, client);
            log.info("Updated client: {}", client);

            return client;
        });
    }

    @Override
    public Optional<Client> findById(long id) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
            log.info("Client: {}", clientOptional);

            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientsList = clientDataTemplate.findAll(session);
            log.info("Clients list:{}", clientsList);

            return clientsList;
        });
    }

    @Override
    public Client findByUsername(String username) {
        var optional = Optional.ofNullable(transactionManager.doInTransaction(
                session -> clientDataTemplate.findByEntityField(session,
                        "username", username)));

        return optional.orElseThrow(() -> new RuntimeException(
                "Client with username " + username + " not found"));
    }

    @Override
    public boolean authenticate(String username, String password) {
        try {
            findByUsername(username);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Client createAdmin(String username, String password) {
        return transactionManager.doInTransaction(session -> {
            var random = UUID.randomUUID().toString().substring(12);
            var client = Client.builder()
                    .name("Name-" + random)
                    .username(username)
                    .password(password)
                    .address(new Address("Address-" + random))
                    .build();

            session.persist(client);

            var phones = Collections.singleton(
                    new Phone(null, "+7666666666", client));

            client.setPhones(phones);
            session.persist(client);

            return client;
        });
    }
}
