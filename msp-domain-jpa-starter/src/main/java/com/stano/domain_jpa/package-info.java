/**
 * Top-level JPA bootstrap for the platform: the {@code @EnableJpa} annotation and the default
 * Spring configuration it imports (auditing, transaction management, and Hibernate defaults).
 *
 * <p>Application modules opt into JPA support by annotating a configuration class with
 * {@code @EnableJpa}; the annotation wires in entity scanning, routing repositories, and the
 * defaults defined in this package.
 */
package com.stano.domain_jpa;
