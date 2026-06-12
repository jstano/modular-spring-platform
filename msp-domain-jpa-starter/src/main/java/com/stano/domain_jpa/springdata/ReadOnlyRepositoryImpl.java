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

public class ReadOnlyRepositoryImpl<T extends AbstractEntity<ID>, ID extends EntityId>
    extends SimpleJpaRepository<T, UUID> implements ReadOnlyRepository<T, ID> {

  public ReadOnlyRepositoryImpl(
      JpaEntityInformation<T, UUID> entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);
  }

  @Override
  public boolean exists(ID id) {
    return existsById(id.value());
  }

  @Override
  public Optional<T> findById(ID id) {
    return findById(id.value());
  }

  @Override
  public T get(ID id) {
    return findById(id.value()).orElseThrow(EntityNotFoundException::new);
  }

  @Override
  public T getReference(ID id) {
    return getReferenceById(id.value());
  }

  @Override
  public List<T> findAll(Iterable<ID> ids) {
    List<UUID> uuids = StreamSupport.stream(ids.spliterator(), false).map(EntityId::value).toList();
    return findAllById(uuids);
  }
}
