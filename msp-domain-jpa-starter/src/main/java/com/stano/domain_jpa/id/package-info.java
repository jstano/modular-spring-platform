/**
 * Typed, UUID-backed identifiers for entities.
 *
 * <p>{@link com.stano.domain_jpa.id.EntityId} is the base class extended by per-entity id types,
 * and {@link com.stano.domain_jpa.id.EntityIdAttributeConverter} is the base JPA converter used to
 * persist them as their underlying {@link java.util.UUID}. {@link
 * com.stano.domain_jpa.id.DatabaseId} is a simpler, generic UUID identifier with an
 * automatically-applied converter for cases that don't need a dedicated type per entity.
 */
package com.stano.domain_jpa.id;
