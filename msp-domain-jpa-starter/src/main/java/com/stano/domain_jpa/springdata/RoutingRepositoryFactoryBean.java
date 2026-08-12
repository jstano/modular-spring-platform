package com.stano.domain_jpa.springdata;

import com.stano.domain_jpa.repository.ReadOnlyRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 * Repository factory bean configured as {@code repositoryFactoryBeanClass} by {@link
 * com.stano.domain_jpa.EnableJpa @EnableJpa}, choosing the base implementation class for each
 * repository interface at proxy-creation time: {@link ReadOnlyRepositoryImpl} for interfaces
 * extending {@link ReadOnlyRepository}, and {@link EntityRepositoryImpl} for all others.
 *
 * <p>This lets a single {@code @EnableJpaRepositories} configuration serve both read-only and
 * mutable repository interfaces without applications needing to select an implementation class
 * themselves.
 *
 * @param <T> the repository interface type
 * @param <S> the entity type managed by the repository
 * @param <ID> the entity's identifier type as seen by Spring Data (a raw {@link java.util.UUID})
 */
public class RoutingRepositoryFactoryBean<T extends Repository<S, ID>, S, ID>
    extends JpaRepositoryFactoryBean<T, S, ID> {

  /**
   * Creates the factory bean for the given repository interface.
   *
   * @param repositoryInterface the repository interface to create an implementation for
   */
  public RoutingRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
    super(repositoryInterface);
  }

  /**
   * Creates the {@link RoutingRepositoryFactory} used to select and instantiate the correct base
   * repository implementation for each repository interface.
   *
   * @param entityManager the entity manager to back created repositories with
   * @return a repository factory that routes to {@link ReadOnlyRepositoryImpl} or {@link
   *     EntityRepositoryImpl} as appropriate
   */
  @Override
  protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
    return new RoutingRepositoryFactory(entityManager);
  }

  /**
   * JPA repository factory that selects {@link ReadOnlyRepositoryImpl} as the base implementation
   * class for repository interfaces extending {@link ReadOnlyRepository}, and {@link
   * EntityRepositoryImpl} for all others.
   */
  private static class RoutingRepositoryFactory extends JpaRepositoryFactory {

    /**
     * Creates the factory backed by the given entity manager.
     *
     * @param entityManager the entity manager to back created repositories with
     */
    RoutingRepositoryFactory(EntityManager entityManager) {
      super(entityManager);
    }

    /**
     * Selects the base repository implementation class for a given repository interface.
     *
     * @param metadata metadata describing the repository interface being created
     * @return {@link ReadOnlyRepositoryImpl} if the interface extends {@link ReadOnlyRepository},
     *     otherwise {@link EntityRepositoryImpl}
     */
    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
      if (ReadOnlyRepository.class.isAssignableFrom(metadata.getRepositoryInterface())) {
        return ReadOnlyRepositoryImpl.class;
      }
      return EntityRepositoryImpl.class;
    }
  }
}
