package com.stano.domain_jpa.jpa.converters;

/**
 * Marker interface identifying this package for entity scanning.
 *
 * <p>{@code EnableJpaRegistrar} uses {@code JpaConvertersPackage.class.getPackageName()} to ensure
 * this package's {@code @Converter} classes are always picked up, regardless of which packages an
 * application registers via {@code @EnableJpa}.
 */
public interface JpaConvertersPackage {}
