package ru.otus.config;

import lombok.Getter;
import org.hibernate.cfg.Configuration;
import ru.otus.data.core.repository.HibernateUtils;
import ru.otus.data.core.sessionmanager.TransactionManager;
import ru.otus.data.core.sessionmanager.TransactionManagerHibernate;


@Getter
public class HibernateDataSourceConfig {

    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    private final Configuration configuration;
    private final TransactionManager transactionManager;


    public HibernateDataSourceConfig(Class<?>... entities) {
        configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        transactionManager = new TransactionManagerHibernate(
                HibernateUtils.buildSessionFactory(configuration, entities));
    }


}
