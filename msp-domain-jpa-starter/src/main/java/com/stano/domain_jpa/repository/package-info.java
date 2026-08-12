/**
 * Repository contracts built on typed entity ids: {@link
 * com.stano.domain_jpa.repository.ReadOnlyRepository} for read-only access and {@link
 * com.stano.domain_jpa.repository.EntityRepository} adding mutation operations. Application
 * repositories extend these instead of Spring Data's {@code JpaRepository} directly so that lookups
 * and mutations take the entity's typed id rather than a raw {@link java.util.UUID}.
 */
package com.stano.domain_jpa.repository;
