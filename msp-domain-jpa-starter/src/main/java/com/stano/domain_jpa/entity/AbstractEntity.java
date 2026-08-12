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

/**
 * Base class for JPA entities providing a typed identifier, audit timestamps, and an
 * optimistic-locking version.
 *
 * <p>Extend this instead of mapping {@code @Id}/{@code @Version} fields by hand. Subclasses must
 * implement {@link #typedId(UUID)} to wrap the entity's raw {@link UUID} in their own {@link
 * EntityId} subtype. The id is generated eagerly in the constructor using {@link
 * EntityIdGenerator}, so it is available even before the entity is persisted.
 *
 * <p>Equality and hash code are identity-based on the entity's id, using {@code
 * Hibernate.getClass(...)} instead of {@code getClass()} so that comparisons work correctly across
 * Hibernate proxies.
 *
 * @param <ID> the entity-specific {@link EntityId} subtype used as this entity's identifier
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity<ID extends EntityId> {
  @Id private UUID id;

  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;

  @Version private int version;

  /**
   * Returns this entity's typed identifier.
   *
   * @return the entity id wrapped in its typed form, or {@code null} if the raw id is not set
   */
  public ID getId() {
    return id == null ? null : typedId(id);
  }

  /**
   * Returns when this entity was first persisted.
   *
   * @return the creation timestamp, or {@code null} if not yet persisted
   */
  public Instant getCreatedAt() {
    return createdAt;
  }

  /**
   * Returns when this entity was last modified.
   *
   * @return the last-modified timestamp, or {@code null} if not yet persisted
   */
  public Instant getUpdatedAt() {
    return updatedAt;
  }

  /**
   * Returns the optimistic-locking version used to detect concurrent modification.
   *
   * @return the current version number
   */
  public int getVersion() {
    return version;
  }

  /** Creates a new entity, eagerly generating its id via {@link EntityIdGenerator}. */
  protected AbstractEntity() {
    this.id = EntityIdGenerator.generate();
  }

  /**
   * Compares entities by id and runtime type, resolving Hibernate proxies to their real class so
   * that proxy and non-proxy instances of the same entity compare equal.
   *
   * @param other the object to compare against
   * @return {@code true} if {@code other} is the same entity type with the same id
   */
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

  /**
   * Returns a hash code consistent with {@link #equals}, based on the entity id when set, or the
   * resolved entity class otherwise.
   *
   * @return the hash code for this entity
   */
  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : org.hibernate.Hibernate.getClass(this).hashCode();
  }

  /**
   * Wraps a raw identifier value in this entity's typed {@link EntityId} subtype.
   *
   * @param value the raw UUID identifier
   * @return the typed identifier
   */
  protected abstract ID typedId(UUID value);
}
