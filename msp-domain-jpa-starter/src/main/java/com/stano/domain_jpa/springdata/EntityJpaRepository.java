package com.stano.domain_jpa.springdata;

import com.stano.domain_jpa.entity.AbstractEntity;
import com.stano.domain_jpa.id.EntityId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Extends Spring Data's {@link JpaRepository} with default methods that accept the entity's typed
 * {@link EntityId} in addition to the raw {@link UUID} operations {@code JpaRepository} already
 * provides. Every default method here simply unwraps the typed id(s) via {@link EntityId#value()}
 * and delegates to the corresponding {@code JpaRepository} method.
 *
 * @param <T> the entity type managed by this repository
 * @param <ID> the entity's typed identifier type
 */
@NoRepositoryBean
public interface EntityJpaRepository<T extends AbstractEntity<ID>, ID extends EntityId>
    extends JpaRepository<T, UUID> {
  /**
   * Retrieves the entity with the given typed id, if it exists.
   *
   * @param id the typed entity id to look up
   * @return the matching entity, or {@link Optional#empty()} if none exists
   */
  default Optional<T> findById(ID id) {
    return findById(id.value());
  }

  /**
   * Retrieves all entities matching the given typed ids.
   *
   * @param ids the typed entity ids to look up
   * @return the matching entities
   */
  default List<T> findAllByTypedId(Iterable<? extends ID> ids) {
    List<UUID> uuids = StreamSupport.stream(ids.spliterator(), false).map(EntityId::value).toList();
    return findAllById(uuids);
  }

  /**
   * Checks whether an entity with the given typed id exists.
   *
   * @param id the typed entity id to look up
   * @return {@code true} if an entity with the given id exists
   */
  default boolean existsById(ID id) {
    return existsById(id.value());
  }

  /**
   * Retrieves a lazily-loaded reference to the entity with the given typed id.
   *
   * @param id the typed entity id to look up
   * @return a reference to the matching entity
   */
  default T getReferenceById(ID id) {
    return getReferenceById(id.value());
  }

  /**
   * Deletes the entity with the given typed id.
   *
   * @param id the typed id of the entity to delete
   */
  default void deleteById(ID id) {
    deleteById(id.value());
  }

  /**
   * Deletes all entities matching the given typed ids.
   *
   * @param ids the typed ids of the entities to delete
   */
  default void deleteAllByTypedId(Iterable<? extends ID> ids) {
    List<UUID> uuids = StreamSupport.stream(ids.spliterator(), false).map(EntityId::value).toList();
    deleteAllById(uuids);
  }

  /**
   * Deletes all entities matching the given typed ids in a single batch operation.
   *
   * @param ids the typed ids of the entities to delete
   */
  default void deleteAllInBatchByTypedId(Iterable<? extends ID> ids) {
    List<T> entities = findAllByTypedId(ids);
    deleteInBatch(entities);
  }
}
