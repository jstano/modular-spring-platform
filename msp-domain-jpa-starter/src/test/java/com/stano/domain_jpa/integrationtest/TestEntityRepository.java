package com.stano.domain_jpa.integrationtest;

import com.stano.domain_jpa.repository.EntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestEntityRepository extends EntityRepository<TestEntity, TestEntityId> {}
