package com.stano.domain_jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public abstract class BaseJpaTest {
  @PersistenceContext protected EntityManager entityManager;
}
