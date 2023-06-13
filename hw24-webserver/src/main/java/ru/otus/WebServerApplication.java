package ru.otus;

import org.hibernate.cfg.Configuration;
import ru.otus.data.core.repository.DataTemplateHibernate;
import ru.otus.data.core.repository.HibernateUtils;
import ru.otus.data.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.data.crm.model.Address;
import ru.otus.data.crm.model.Client;
import ru.otus.data.crm.model.Phone;
import ru.otus.data.crm.service.ClientServiceImpl;
import ru.otus.data.util.HibernateFlywayMigrations;
import ru.otus.web.ClientsSecurityWebServer;

public class WebServerApplication {


    public static void main(String[] args) throws Exception {
        var transactionManager = initializeDatabase();
        var clientService = new ClientServiceImpl(transactionManager,
                new DataTemplateHibernate<>(Client.class));
        var server = new ClientsSecurityWebServer(clientService);

        server.start();
        server.join();
    }

    private static TransactionManagerHibernate initializeDatabase() {
        var configuration =
                new Configuration().configure(HibernateUtils.CONFIG_FILE);

        HibernateFlywayMigrations.migrate(configuration);

        var sessionFactory =
                HibernateUtils.buildSessionFactory(configuration, Client.class,
                        Address.class, Phone.class);

        return new TransactionManagerHibernate(sessionFactory);
    }

}
