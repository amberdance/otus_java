package ru.otus.service;

import ru.otus.cachehw.HwCache;
import ru.otus.model.Client;

import java.util.List;
import java.util.Optional;

public class DbCacheServiceClientImpl implements DBServiceClient {

    private final DBServiceClient serviceClient;
    private final HwCache<Long, Client> customCache;

    public DbCacheServiceClientImpl(DBServiceClient serviceClient,
                                    HwCache<Long, Client> customCache) {
        this.serviceClient = serviceClient;
        this.customCache = customCache;
    }

    @Override
    public Client saveClient(Client client) {
        var savedClient = serviceClient.saveClient(client);
        customCache.put(savedClient.getId(), savedClient);

        return savedClient;
    }

    @Override
    public Optional<Client> getClient(long id) {
        var client = customCache.get(id);

        if (client == null) {
            var loadClientOptional = serviceClient.getClient(id);
            loadClientOptional.ifPresent(value -> customCache.put(id, value));
            return loadClientOptional;
        }

        return Optional.of(client);
    }

    @Override
    public List<Client> findAll() {
        var clients = serviceClient.findAll();
        clients.forEach(c -> customCache.put(c.getId(), c));

        return clients;
    }
}
