package ru.otus.data.core.repository;

import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.Arrays;

@UtilityClass
public final class HibernateUtils {

    public static final String CONFIG_FILE = "hibernate.cfg.xml";


    public static SessionFactory buildSessionFactory(
            Configuration configuration, Class<?>... annotatedClasses) {
        MetadataSources metadataSources =
                new MetadataSources(createServiceRegistry(configuration));
        Arrays.stream(annotatedClasses)
                .forEach(metadataSources::addAnnotatedClass);

        Metadata metadata = metadataSources.getMetadataBuilder().build();
        return metadata.getSessionFactoryBuilder().build();
    }

    private static StandardServiceRegistry createServiceRegistry(
            Configuration configuration) {
        return new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
    }
}
