package com.stano.domain_jpa.springdata.repository;

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

public class EntityRepositoryImpl<T extends AbstractEntity<ID>, ID extends EntityId>
    extends SimpleJpaRepository<T, UUID>
    implements EntityRepository<T, ID> {

  public EntityRepositoryImpl(JpaEntityInformation<T, UUID> entityInformation,
      EntityManager entityManager) {
    super(entityInformation, entityManager);
  }

  @Override
  public <S extends T> void add(S entity) {
    save(entity);
  }

  @Override
  public <S extends T> void addAll(Iterable<S> entities) {
    saveAll(entities);
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
    List<UUID> uuids = StreamSupport.stream(ids.spliterator(), false)
        .map(EntityId::value)
        .toList();
    return findAllById(uuids);
  }

  @Override
  public void remove(ID id) {
    deleteById(id.value());
  }

  @Override
  public void remove(T entity) {
    delete(entity);
  }

  @Override
  public void removeAll() {
    deleteAll();
  }

  @Override
  public void removeAll(Iterable<? extends T> entities) {
    deleteAll(entities);
  }

  @Override
  public void removeAllById(Iterable<ID> ids) {
    List<UUID> uuids = StreamSupport.stream(ids.spliterator(), false)
        .map(EntityId::value)
        .toList();
    deleteAllById(uuids);
  }
}
