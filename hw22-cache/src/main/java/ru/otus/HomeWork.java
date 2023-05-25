package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.MyCache;
import ru.otus.core.datasource.DriverManagerDataSource;
import ru.otus.core.executor.DbExecutorImpl;
import ru.otus.core.mapper.DataTemplateJdbc;
import ru.otus.core.mapper.EntityClassMetaDataImpl;
import ru.otus.core.mapper.EntityConverterImpl;
import ru.otus.core.mapper.EntitySQLMetaDataImpl;
import ru.otus.core.mapper.ParamsProviderImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.model.Client;
import ru.otus.service.DBServiceClient;
import ru.otus.service.DbCacheServiceClientImpl;
import ru.otus.service.DbServiceClientImpl;

import javax.sql.DataSource;
import java.util.stream.IntStream;

public class HomeWork {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";
    private static final int CLIENTS_COUNT = 100;

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);

        flywayMigrations(dataSource);

        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();
        var dbServiceClient =
                createClientService(transactionRunner, dbExecutor);

        IntStream.range(0, CLIENTS_COUNT).forEach(
                i -> dbServiceClient.saveClient(new Client("Client " + i)));

        var start = System.currentTimeMillis();
        test(dbServiceClient);

        var end = System.currentTimeMillis();
        var withoutCacheTime = start - end;
        var cacheService =
                new DbCacheServiceClientImpl(dbServiceClient, new MyCache<>());

        start = System.currentTimeMillis();
        test(cacheService);
        end = System.currentTimeMillis();

        var withCacheTime = start - end;

        log.info("-----------------------------------------");
        log.info("Without cache {} ms", withoutCacheTime);
        log.info("With cache {} ms", withCacheTime);

    }

    private static void test(DBServiceClient dbServiceClient) {
        var clients = dbServiceClient.findAll();

        for (var client : clients) {
            dbServiceClient.getClient(client.getId())
                    .orElseThrow(() -> new RuntimeException(
                            "Client not found, id:" + client.getId()));
        }
    }

    private static DBServiceClient createClientService(
            TransactionRunnerJdbc transactionRunner,
            DbExecutorImpl dbExecutor) {
        var entityClassMetaDataClient =
                new EntityClassMetaDataImpl<>(Client.class);
        var paramsProviderClient =
                new ParamsProviderImpl<>(entityClassMetaDataClient);
        var entityConverterClient =
                new EntityConverterImpl<>(entityClassMetaDataClient);
        var entitySQLMetaDataClient =
                new EntitySQLMetaDataImpl(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<>(
                dbExecutor, entitySQLMetaDataClient, paramsProviderClient,
                entityConverterClient);

        return new DbServiceClientImpl(transactionRunner, dataTemplateClient);
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("Db migration started...");

        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();

        log.info("Db migration finished.");
    }
}
