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

@NoRepositoryBean
public interface EntityRepository<T extends AbstractEntity<ID>, ID extends EntityId>
    extends Repository<T, UUID> {

  <S extends T> void add(S entity);

  <S extends T> void addAll(Iterable<S> entities);

  boolean exists(ID id);

  T get(ID id);

  T getReference(ID id);

  Optional<T> findById(ID id);

  List<T> findAll();

  List<T> findAll(Iterable<ID> ids);

  List<T> findAll(Sort sort);

  Page<T> findAll(Pageable pageable);

  Optional<T> findOne(Specification<T> spec);

  List<T> findAll(Specification<T> spec);

  List<T> findAll(Specification<T> spec, Sort sort);

  Page<T> findAll(Specification<T> spec, Pageable pageable);

  long count();

  long count(Specification<T> spec);

  void remove(ID id);

  void remove(T entity);

  void removeAll();

  void removeAll(Iterable<? extends T> entities);

  void removeAllById(Iterable<ID> ids);
}
