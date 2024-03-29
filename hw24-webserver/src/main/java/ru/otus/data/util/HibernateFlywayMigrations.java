package ru.otus.data.util;

import lombok.experimental.UtilityClass;
import org.flywaydb.core.Flyway;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateFlywayMigrations {


    public static void migrate(Configuration configuration) {
        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName =
                configuration.getProperty("hibernate.connection.username");
        var dbPassword =
                configuration.getProperty("hibernate.connection.password");

        Flyway.configure()
                .dataSource(dbUrl, dbUserName, dbPassword)
                .locations("classpath:/db/migration")
                .baselineOnMigrate(true)
                .load()
                .migrate();
    }

}
