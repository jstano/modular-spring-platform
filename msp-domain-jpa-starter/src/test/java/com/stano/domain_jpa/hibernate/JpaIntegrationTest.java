package com.stano.domain_jpa.hibernate;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class JpaIntegrationTest {

    @Test
    void allUserTypesShouldPersistProperly() throws java.net.MalformedURLException {
        var entityManager = HibernateUtil.getEntityManager();
        var session = entityManager.unwrap(Session.class);

        var testEntity = new TestEntity2();
        testEntity.setBirthDate(LocalDate.of(2014, 1, 1));
        testEntity.setBirthDateTime(LocalDateTime.of(2014, 1, 1, 8, 30, 45));
        testEntity.setBirthTime(LocalTime.of(8, 30, 45));
        testEntity.setBirthDayOfWeek(DayOfWeek.WEDNESDAY);
        testEntity.setLocale(Locale.US);
        testEntity.setZoneId(ZoneId.of("UTC"));
        testEntity.setUrl(new java.net.URL("https://test.com"));
        testEntity.setDecimal(new java.math.BigDecimal("12345.6789"));
        testEntity.setActive(true);

        doInTransaction(entityManager, () -> {
            entityManager.persist(testEntity);
            entityManager.flush();
            entityManager.clear();
            return null;
        });

        TestEntity2 result = (TestEntity2) doInTransaction(entityManager, () -> entityManager.find(TestEntity2.class, testEntity.getId()));

        assertThat(result.getId()).isEqualTo(testEntity.getId());
        assertThat(result.getBirthDate()).isEqualTo(testEntity.getBirthDate());
        assertThat(result.getBirthDateTime()).isEqualTo(testEntity.getBirthDateTime());
        assertThat(result.getBirthTime()).isEqualTo(testEntity.getBirthTime());
        assertThat(result.getBirthDayOfWeek()).isEqualTo(DayOfWeek.WEDNESDAY);
        assertThat(result.getLocale()).isEqualTo(Locale.US);
        assertThat(result.getZoneId()).isEqualTo(ZoneId.of("UTC"));
        assertThat(result.getUrl().toString()).isEqualTo("https://test.com");
        assertThat(result.getDecimal()).isEqualTo(new java.math.BigDecimal("12345.6789"));
        assertThat(result.isActive()).isTrue();

        assertThat(entityManager.createNativeQuery("select active from TestEntity2").getSingleResult()).isEqualTo(true);

        doInTransaction(entityManager, () -> {
            entityManager.remove(result);
            return null;
        });
        entityManager.close();
    }

    @Test
    void shouldBeAbleToQueryBooleanStoredAsNativeBoolean() {
        var entityManager = HibernateUtil.getEntityManager();
        var session = entityManager.unwrap(Session.class);

        createSimpleEntity(entityManager);

        var result = (TestEntity2) doInTransaction(entityManager, () -> {
            return entityManager.createQuery("select t from TestEntity2 t where active = :active")
                .setParameter("active", true)
                .getSingleResult();
        });

        assertThat(result).isNotNull();

        try {
            doInTransaction(entityManager, () -> {
                entityManager.remove(result);
                return null;
            });
        } finally {
            entityManager.close();
        }
    }

    private void createSimpleEntity(EntityManager entityManager) {
        var testEntity = new TestEntity2();
        testEntity.setActive(true);

        doInTransaction(entityManager, () -> {
            entityManager.persist(testEntity);
            entityManager.flush();
            entityManager.clear();
            return null;
        });
    }

    private Object doInTransaction(EntityManager entityManager, TransactionCallback callback) {
        var tx = entityManager.getTransaction();
        tx.begin();

        try {
            Object result = callback.execute();
            tx.commit();
            return result;
        } catch (Exception x) {
            tx.rollback();
            throw x;
        }
    }

    @FunctionalInterface
    interface TransactionCallback {
        Object execute();
    }
}
