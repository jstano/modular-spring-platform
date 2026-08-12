package com.stano.domain_jpa.springdata;

import com.stano.domain_jpa.entity.AbstractEntity;
import com.stano.domain_jpa.id.EntityId;
import com.stano.domain_jpa.repository.ReadOnlyRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

/**
 * Default {@link ReadOnlyRepository} implementation, built on Spring Data's {@link
 * SimpleJpaRepository} and translating each typed-id lookup into the corresponding raw-{@link UUID}
 * lookup. Instantiated automatically per read-only repository interface by {@link
 * RoutingRepositoryFactoryBean}; not normally created directly by application code.
 *
 * @param <T> the entity type managed by this repository
 * @param <ID> the entity's typed identifier type
 */
public class ReadOnlyRepositoryImpl<T extends AbstractEntity<ID>, ID extends EntityId>
    extends SimpleJpaRepository<T, UUID> implements ReadOnlyRepository<T, ID> {

  /**
   * Creates a repository for the given entity metadata and entity manager, as required by {@link
   * SimpleJpaRepository}.
   *
   * @param entityInformation JPA metadata for the managed entity type
   * @param entityManager the entity manager to use for persistence operations
   */
  public ReadOnlyRepositoryImpl(
      JpaEntityInformation<T, UUID> entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);
  }

  /**
   * Checks whether an entity with the given id exists.
   *
   * @param id the entity id to look up
   * @return {@code true} if an entity with the given id exists
   */
  @Override
  public boolean exists(ID id) {
    return existsById(id.value());
  }

  /**
   * Retrieves the entity with the given id, if it exists.
   *
   * @param id the entity id to look up
   * @return the matching entity, or {@link Optional#empty()} if none exists
   */
  @Override
  public Optional<T> findById(ID id) {
    return findById(id.value());
  }

  /**
   * Retrieves the entity with the given id.
   *
   * @param id the entity id to look up
   * @return the matching entity
   * @throws EntityNotFoundException if no entity with the given id exists
   */
  @Override
  public T get(ID id) {
    return findById(id.value()).orElseThrow(EntityNotFoundException::new);
  }

  /**
   * Retrieves a lazily-loaded reference to the entity with the given id.
   *
   * @param id the entity id to look up
   * @return a reference to the matching entity
   */
  @Override
  public T getReference(ID id) {
    return getReferenceById(id.value());
  }

  /**
   * Retrieves all entities matching the given ids.
   *
   * @param ids the entity ids to look up
   * @return the matching entities
   */
  @Override
  public List<T> findAll(Iterable<ID> ids) {
    List<UUID> uuids = StreamSupport.stream(ids.spliterator(), false).map(EntityId::value).toList();
    return findAllById(uuids);
  }
}
