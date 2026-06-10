package com.stano.domain_jpa.springdata;

import com.stano.domain_jpa.repository.ReadOnlyRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

public class RoutingRepositoryFactoryBean<T extends Repository<S, ID>, S, ID>
    extends JpaRepositoryFactoryBean<T, S, ID> {

  public RoutingRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
    super(repositoryInterface);
  }

  @Override
  protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
    return new RoutingRepositoryFactory(entityManager);
  }

  private static class RoutingRepositoryFactory extends JpaRepositoryFactory {

    RoutingRepositoryFactory(EntityManager entityManager) {
      super(entityManager);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
      if (ReadOnlyRepository.class.isAssignableFrom(metadata.getRepositoryInterface())) {
        return ReadOnlyRepositoryImpl.class;
      }
      return EntityRepositoryImpl.class;
    }
  }
}
