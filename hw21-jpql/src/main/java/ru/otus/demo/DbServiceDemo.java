package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientImpl;

public class DbServiceDemo {

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static void main(String[] args) {
        var connectionConfig = configureConnection();
        migrateTables(connectionConfig);

        var dbServiceClient = configureDbServiceClient(connectionConfig);
        handleClientLogic(dbServiceClient);
    }

    private static Configuration configureConnection() {
        return new Configuration().configure(HIBERNATE_CFG_FILE);
    }

    private static void migrateTables(Configuration configuration) {
        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
    }

    private static DBServiceClient configureDbServiceClient(Configuration configuration) {
        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        return new DbServiceClientImpl(transactionManager, clientTemplate);
    }

    private static void handleClientLogic(DBServiceClient dbServiceClient) {
        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));

        log.info("clientSecondSelected:{}", clientSecondSelected);

        dbServiceClient.saveClient(new Client(clientSecondSelected.getId(), "dbServiceSecondUpdated"));

        var clientUpdated = dbServiceClient.getClient(clientSecondSelected.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecondSelected.getId()));

        log.info("clientUpdated:{}", clientUpdated);
        log.info("All clients");

        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));
    }
}
