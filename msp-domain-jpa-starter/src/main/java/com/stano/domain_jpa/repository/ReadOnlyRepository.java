package com.stano.domain_jpa.repository;

import com.stano.domain_jpa.entity.AbstractEntity;
import com.stano.domain_jpa.id.EntityId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/**
 * Read-only repository contract for {@link AbstractEntity} types, addressed by their typed {@link
 * EntityId} rather than a raw {@link UUID}.
 *
 * <p>Application repositories that only need to query, not mutate, entities extend this interface
 * (typically backed by {@link com.stano.domain_jpa.springdata.ReadOnlyRepositoryImpl}) instead of
 * exposing a full {@link EntityRepository}.
 *
 * @param <T> the entity type managed by this repository
 * @param <ID> the entity's typed identifier type
 */
@NoRepositoryBean
public interface ReadOnlyRepository<T extends AbstractEntity<ID>, ID extends EntityId>
    extends Repository<T, UUID> {

  /**
   * Checks whether an entity with the given id exists.
   *
   * @param id the entity id to look up
   * @return {@code true} if an entity with the given id exists
   */
  boolean exists(ID id);

  /**
   * Retrieves the entity with the given id, if it exists.
   *
   * @param id the entity id to look up
   * @return the matching entity, or {@link Optional#empty()} if none exists
   */
  Optional<T> findById(ID id);

  /**
   * Retrieves the entity with the given id.
   *
   * @param id the entity id to look up
   * @return the matching entity
   * @throws jakarta.persistence.EntityNotFoundException if no entity with the given id exists
   */
  T get(ID id);

  /**
   * Retrieves a lazily-loaded reference to the entity with the given id, without necessarily
   * hitting the database.
   *
   * @param id the entity id to look up
   * @return a reference to the matching entity
   */
  T getReference(ID id);

  /**
   * Retrieves all entities of this type.
   *
   * @return all entities
   */
  List<T> findAll();

  /**
   * Retrieves all entities matching the given ids.
   *
   * @param ids the entity ids to look up
   * @return the matching entities
   */
  List<T> findAll(Iterable<ID> ids);

  /**
   * Retrieves all entities of this type, sorted as specified.
   *
   * @param sort the sort order to apply
   * @return all entities, sorted as specified
   */
  List<T> findAll(Sort sort);

  /**
   * Retrieves a page of entities of this type.
   *
   * @param pageable the paging information to apply
   * @return the requested page of entities
   */
  Page<T> findAll(Pageable pageable);

  /**
   * Retrieves a single entity matching the given specification, if any.
   *
   * @param spec the query specification to match
   * @return the matching entity, or {@link Optional#empty()} if none matches
   */
  Optional<T> findOne(Specification<T> spec);

  /**
   * Retrieves all entities matching the given specification.
   *
   * @param spec the query specification to match
   * @return the matching entities
   */
  List<T> findAll(Specification<T> spec);

  /**
   * Retrieves all entities matching the given specification, sorted as specified.
   *
   * @param spec the query specification to match
   * @param sort the sort order to apply
   * @return the matching entities, sorted as specified
   */
  List<T> findAll(Specification<T> spec, Sort sort);

  /**
   * Retrieves a page of entities matching the given specification.
   *
   * @param spec the query specification to match
   * @param pageable the paging information to apply
   * @return the requested page of matching entities
   */
  Page<T> findAll(Specification<T> spec, Pageable pageable);

  /**
   * Counts all entities of this type.
   *
   * @return the total number of entities
   */
  long count();

  /**
   * Counts all entities matching the given specification.
   *
   * @param spec the query specification to match
   * @return the number of matching entities
   */
  long count(Specification<T> spec);
}
