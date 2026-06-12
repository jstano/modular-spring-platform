package com.stano.domain_jpa.entity;

import com.stano.domain_jpa.id.EntityId;
import com.stano.domain_jpa.id.EntityIdGenerator;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity<ID extends EntityId> {
  @Id private UUID id;

  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;

  @Version private int version;

  public ID getId() {
    return id == null ? null : typedId(id);
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public int getVersion() {
    return version;
  }

  protected AbstractEntity() {
    this.id = EntityIdGenerator.generate();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (other == null) {
      return false;
    }

    if (org.hibernate.Hibernate.getClass(this) != org.hibernate.Hibernate.getClass(other)) {
      return false;
    }

    return id != null && id.equals(((AbstractEntity<?>) other).id);
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : org.hibernate.Hibernate.getClass(this).hashCode();
  }

  protected abstract ID typedId(UUID value);
}
