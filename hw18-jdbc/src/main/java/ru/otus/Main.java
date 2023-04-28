package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Manager;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.crm.service.DbServiceManagerImpl;
import ru.otus.jdbc.mapper.DataTemplateJdbc;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntityClassMetaDataImpl;
import ru.otus.jdbc.mapper.EntitySQLMetaDataImpl;

import javax.sql.DataSource;

public class Main {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final DbExecutor dbExecutor = new DbExecutorImpl();
    private static final DataSource dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
    private static final TransactionRunner transactionRunner = new TransactionRunnerJdbc(dataSource);


    public static void main(String[] args) {
        flywayMigrations();
//        processClientLogic();
//        processManagerLogic();
    }

    private static void processClientLogic() {
        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>();

        var entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<Client>(dbExecutor, entitySQLMetaDataClient);
        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);

        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));

        log.info("clientSecondSelected:{}", clientSecondSelected);
    }

    private static void processManagerLogic() {
        EntityClassMetaData<Manager> entityClassMetaDataManager = new EntityClassMetaDataImpl<>();

        var entitySQLMetaDataManager = new EntitySQLMetaDataImpl(entityClassMetaDataManager);
        var dataTemplateManager = new DataTemplateJdbc<Manager>(dbExecutor, entitySQLMetaDataManager);
        var dbServiceManager = new DbServiceManagerImpl(transactionRunner, dataTemplateManager);

        dbServiceManager.saveManager(new Manager("ManagerFirst"));

        var managerSecond = dbServiceManager.saveManager(new Manager("ManagerSecond"));
        var managerSecondSelected = dbServiceManager.getManager(managerSecond.getNo())
                .orElseThrow(() -> new RuntimeException("Manager not found, id:" + managerSecond.getNo()));

        log.info("managerSecondSelected:{}", managerSecondSelected);
    }


    private static void flywayMigrations() {
        log.info("Db migration started...");

        Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load()
                .migrate();

        log.info("Db migration finished.");
    }
}
