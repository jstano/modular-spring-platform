/**
 * Spring Data implementation plumbing for the typed-id repository contracts in {@link
 * com.stano.domain_jpa.repository}, including the {@link
 * com.stano.domain_jpa.springdata.RoutingRepositoryFactoryBean} that chooses between the read-only
 * and mutable repository implementations at proxy-creation time.
 */
package com.stano.domain_jpa.springdata;
