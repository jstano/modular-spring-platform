package com.stano.domain_jpa.springdata;

import com.stano.domain_jpa.entity.AbstractEntity;
import com.stano.domain_jpa.id.EntityId;
import com.stano.domain_jpa.repository.EntityRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

/**
 * Default {@link EntityRepository} implementation, built on Spring Data's {@link
 * SimpleJpaRepository} and translating each typed-id operation into the corresponding raw-{@link
 * UUID} operation. Instantiated automatically per repository interface by {@link
 * RoutingRepositoryFactoryBean}; not normally created directly by application code.
 *
 * @param <T> the entity type managed by this repository
 * @param <ID> the entity's typed identifier type
 */
public class EntityRepositoryImpl<T extends AbstractEntity<ID>, ID extends EntityId>
    extends SimpleJpaRepository<T, UUID> implements EntityRepository<T, ID> {

  /**
   * Creates a repository for the given entity metadata and entity manager, as required by {@link
   * SimpleJpaRepository}.
   *
   * @param entityInformation JPA metadata for the managed entity type
   * @param entityManager the entity manager to use for persistence operations
   */
  public EntityRepositoryImpl(
      JpaEntityInformation<T, UUID> entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);
  }

  /**
   * Persists a new or existing entity.
   *
   * @param entity the entity to save
   */
  @Override
  public <S extends T> void add(S entity) {
    save(entity);
  }

  /**
   * Persists a batch of new or existing entities.
   *
   * @param entities the entities to save
   */
  @Override
  public <S extends T> void addAll(Iterable<S> entities) {
    saveAll(entities);
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

  /**
   * Deletes the entity with the given id.
   *
   * @param id the id of the entity to delete
   */
  @Override
  public void remove(ID id) {
    deleteById(id.value());
  }

  /**
   * Deletes the given entity.
   *
   * @param entity the entity to delete
   */
  @Override
  public void remove(T entity) {
    delete(entity);
  }

  /** Deletes all entities of this type. */
  @Override
  public void removeAll() {
    deleteAll();
  }

  /**
   * Deletes the given entities.
   *
   * @param entities the entities to delete
   */
  @Override
  public void removeAll(Iterable<? extends T> entities) {
    deleteAll(entities);
  }

  /**
   * Deletes all entities matching the given ids.
   *
   * @param ids the ids of the entities to delete
   */
  @Override
  public void removeAllById(Iterable<ID> ids) {
    List<UUID> uuids = StreamSupport.stream(ids.spliterator(), false).map(EntityId::value).toList();
    deleteAllById(uuids);
  }
}
