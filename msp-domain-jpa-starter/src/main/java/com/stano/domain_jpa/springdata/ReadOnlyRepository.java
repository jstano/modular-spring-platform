package com.stano.domain_jpa.springdata;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface ReadOnlyRepository<T, ID> extends Repository<T, ID> {
  /**
   * Get a reference to the entity by its ID (lazy, does not hit DB immediately).
   */
  T getReferenceById(ID id);

  /**
   * Find an entity by its ID.
   */
  Optional<T> findById(ID id);

  /**
   * Retrieve all entities of this type.
   */
  List<T> findAll();

  /**
   * Check if an entity exists by ID.
   */
  boolean existsById(ID id);

  /**
   * Count all entities of this type.
   */
  long count();
}
