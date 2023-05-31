package ru.otus.data.crm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.data.core.repository.DataTemplate;
import ru.otus.data.core.sessionmanager.TransactionManager;
import ru.otus.data.crm.model.Client;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class DbServiceClientImpl implements DBServiceClient {


    private final TransactionManager transactionManager;
    private final DataTemplate<Client> clientDataTemplate;


    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            if (client.getId() == null) {
                clientDataTemplate.insert(session, client);
                log.info("created client: {}", client);
                return client;
            }
            clientDataTemplate.update(session, client);
            log.info("updated client: {}", client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }
}
