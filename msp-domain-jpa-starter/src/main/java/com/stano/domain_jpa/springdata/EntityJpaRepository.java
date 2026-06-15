package com.stano.domain_jpa.springdata;

import com.stano.domain_jpa.entity.AbstractEntity;
import com.stano.domain_jpa.id.EntityId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface EntityJpaRepository<T extends AbstractEntity<ID>, ID extends EntityId>
    extends JpaRepository<T, UUID> {
  default Optional<T> findById(ID id) {
    return findById(id.value());
  }

  default List<T> findAllByTypedId(Iterable<? extends ID> ids) {
    List<UUID> uuids = StreamSupport.stream(ids.spliterator(), false).map(EntityId::value).toList();
    return findAllById(uuids);
  }

  default boolean existsById(ID id) {
    return existsById(id.value());
  }

  default T getReferenceById(ID id) {
    return getReferenceById(id.value());
  }

  default void deleteById(ID id) {
    deleteById(id.value());
  }

  default void deleteAllByTypedId(Iterable<? extends ID> ids) {
    List<UUID> uuids = StreamSupport.stream(ids.spliterator(), false).map(EntityId::value).toList();
    deleteAllById(uuids);
  }

  default void deleteAllInBatchByTypedId(Iterable<? extends ID> ids) {
    List<T> entities = findAllByTypedId(ids);
    deleteInBatch(entities);
  }
}
