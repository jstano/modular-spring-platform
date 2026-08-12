package com.stano.domain_jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Abstract base class for JPA integration tests, typically used together with {@link JpaTest}.
 *
 * <p>Subclasses get direct access to the test persistence context via {@link #entityManager}, for
 * asserting persisted state or flushing/clearing between test steps.
 */
public abstract class BaseJpaTest {
  /** The JPA entity manager for the test's persistence context. */
  @PersistenceContext protected EntityManager entityManager;
}
