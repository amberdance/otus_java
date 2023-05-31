package ru.otus;

import ru.otus.config.FlywayMigrationsConfig;
import ru.otus.config.HibernateDataSourceConfig;
import ru.otus.data.core.repository.DataTemplateHibernate;
import ru.otus.data.crm.model.Address;
import ru.otus.data.crm.model.Client;
import ru.otus.data.crm.model.Phone;
import ru.otus.data.crm.service.DbServiceClientImpl;

public class WebServletApplication {

    public static void main(String[] args) {
        var hibernateConfig =
                new HibernateDataSourceConfig(Client.class, Phone.class,
                        Address.class);
        FlywayMigrationsConfig.migrateTables(
                hibernateConfig.getConfiguration());

        var dbServiceClient =
                new DbServiceClientImpl(hibernateConfig.getTransactionManager(),
                        new DataTemplateHibernate<>(Client.class));


        initializeServletContainer(dbServiceClient);
    }


    private static void initializeServletContainer(Object... dependencies) {
        //
    }


}
