/**
 * Hibernate-level integration points: a {@link org.hibernate.resource.jdbc.spi.StatementInspector}
 * that tags SQL with the current trace id, and auto-configuration that registers any Spring-managed
 * Hibernate event listeners with the session factory.
 */
package com.stano.domain_jpa.jpa.hibernate;
