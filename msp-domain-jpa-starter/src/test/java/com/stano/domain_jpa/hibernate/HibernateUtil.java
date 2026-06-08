package com.stano.domain_jpa.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
  private static final EntityManagerFactory entityManagerFactory;

  static {
    StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                                         .applySetting("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
                                         .applySetting("hibernate.hbm2ddl.auto", "create")
                                         .applySetting("jakarta.persistence.jdbc.driver", "org.h2.Driver")
                                         .applySetting("jakarta.persistence.jdbc.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")
                                         .applySetting("jakarta.persistence.jdbc.user", "sa")
                                         .applySetting("jakarta.persistence.jdbc.password", "")
                                         .applySetting("hibernate.show_sql", "true")
                                         .build();

    MetadataSources metadataSources = new MetadataSources(registry)
                                        .addAnnotatedClass(TestEntity.class);

    entityManagerFactory = metadataSources.getMetadataBuilder()
                                          .build()
                                          .getSessionFactoryBuilder()
                                          .build()
                                          .unwrap(EntityManagerFactory.class);
  }

  public static EntityManager getEntityManager() {
    return entityManagerFactory.createEntityManager();
  }
}
