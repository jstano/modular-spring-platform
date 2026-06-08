package com.stano.domain_jpa.routable_datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RoutableDataSourceTest {
    private Long savedDatabaseId;

    @BeforeEach
    void setup() {
        savedDatabaseId = DatabaseContextHolder.getDatabaseId();
    }

    @AfterEach
    void cleanup() {
        if (savedDatabaseId != null) {
            DatabaseContextHolder.setDatabaseId(savedDatabaseId);
        } else {
            DatabaseContextHolder.clear();
        }
    }

    @Test
    void determineCurrentLookupKeyShouldReturnNonNullKeyWhenDatabaseContextHolderIsEmpty() {
        RoutableDataSourcesLoader loader = mock(RoutableDataSourcesLoader.class);
        when(loader.loadDataSources()).thenReturn(TestDataSourceLoader.createDataSources(1, 2, 3));
        RoutableDataSource ds = new RoutableDataSource(loader);
        try {
            DatabaseContextHolder.clear();
            assertThat(ds.determineCurrentLookupKey()).isNotNull();
        } finally {
            if (savedDatabaseId != null) {
                DatabaseContextHolder.setDatabaseId(savedDatabaseId);
            }
        }
    }

    @Test
    void getNumberOfDataSourcesShouldReturn3() {
        RoutableDataSourcesLoader loader = mock(RoutableDataSourcesLoader.class);
        when(loader.loadDataSources()).thenReturn(TestDataSourceLoader.createDataSources(1, 2, 3));
        RoutableDataSource ds = new RoutableDataSource(loader);

        assertThat(ds.getNumberOfDataSources()).isEqualTo(3);
    }

    static Stream<RoutableDataSourceTestCase> routableDataSourceTestCases() {
        return Stream.of(
            new RoutableDataSourceTestCase(1L, 1L),
            new RoutableDataSourceTestCase(2L, 2L),
            new RoutableDataSourceTestCase(3L, 3L)
        );
    }

    @ParameterizedTest
    @MethodSource("routableDataSourceTestCases")
    void determineCurrentLookupKeyShouldReturnCorrectKeyFromDatabaseContextHolder(RoutableDataSourceTestCase testCase) {
        RoutableDataSourcesLoader loader = mock(RoutableDataSourcesLoader.class);
        when(loader.loadDataSources()).thenReturn(TestDataSourceLoader.createDataSources(1, 2, 3));
        RoutableDataSource ds = new RoutableDataSource(loader);
        DatabaseContextHolder.setDatabaseId(testCase.databaseId);

        assertThat(ds.determineCurrentLookupKey()).isEqualTo(testCase.expectedResult);
    }

    static Stream<RoutableDataSourceUrlTestCase> routableDataSourceUrlTestCases() {
        return Stream.of(
            new RoutableDataSourceUrlTestCase(1L, "jdbc:h2:mem:database1"),
            new RoutableDataSourceUrlTestCase(2L, "jdbc:h2:mem:database2"),
            new RoutableDataSourceUrlTestCase(3L, "jdbc:h2:mem:database3")
        );
    }

    @ParameterizedTest
    @MethodSource("routableDataSourceUrlTestCases")
    void determineTargetDataSourceShouldReturnCorrectDatasourceBasedOnContext(RoutableDataSourceUrlTestCase testCase) {
        RoutableDataSourcesLoader loader = mock(RoutableDataSourcesLoader.class);
        when(loader.loadDataSources()).thenReturn(TestDataSourceLoader.createDataSources(1, 2, 3));
        RoutableDataSource ds = new RoutableDataSource(loader);
        DatabaseContextHolder.setDatabaseId(testCase.databaseId);

        var dataSource = (HikariDataSource) ds.determineTargetDataSource();
        assertThat(dataSource.getJdbcUrl()).isEqualTo(testCase.expectedUrl);
    }

    @Test
    void determineTargetDataSourceShouldReloadAndDiscoverNewDataSourcesOnMissingKey() {
        RoutableDataSourcesLoader loader = mock(RoutableDataSourcesLoader.class);
        when(loader.loadDataSources())
            .thenReturn(TestDataSourceLoader.createDataSources(1, 2, 3))
            .thenReturn(TestDataSourceLoader.createDataSources(1, 2, 3, 4));
        RoutableDataSource ds = new RoutableDataSource(loader);

        DatabaseContextHolder.setDatabaseId(1L);
        var dataSource1 = ds.determineTargetDataSource();

        DatabaseContextHolder.setDatabaseId(2L);
        var dataSource2 = ds.determineTargetDataSource();

        DatabaseContextHolder.setDatabaseId(3L);
        var dataSource3 = ds.determineTargetDataSource();

        DatabaseContextHolder.setDatabaseId(4L);
        var dataSource4 = ds.determineTargetDataSource();

        assertThat(ds.getNumberOfDataSources()).isEqualTo(4);
        assertThat(((HikariDataSource) dataSource4).getJdbcUrl()).isEqualTo("jdbc:h2:mem:database4");

        DatabaseContextHolder.setDatabaseId(1L);
        assertThat(ds.determineTargetDataSource()).isSameAs(dataSource1);

        DatabaseContextHolder.setDatabaseId(2L);
        assertThat(ds.determineTargetDataSource()).isSameAs(dataSource2);

        DatabaseContextHolder.setDatabaseId(3L);
        assertThat(ds.determineTargetDataSource()).isSameAs(dataSource3);

        DatabaseContextHolder.setDatabaseId(4L);
        assertThat(ds.determineTargetDataSource()).isSameAs(dataSource4);
    }

    @Test
    void determineTargetDataSourceShouldThrowExceptionIfKeyNotFoundAfterReload() {
        RoutableDataSourcesLoader loader = mock(RoutableDataSourcesLoader.class);
        when(loader.loadDataSources()).thenReturn(TestDataSourceLoader.createDataSources(1, 2, 3));
        RoutableDataSource ds = new RoutableDataSource(loader);
        DatabaseContextHolder.setDatabaseId(999L);

        assertThatThrownBy(() -> ds.determineTargetDataSource())
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void refreshShouldReplaceChangedDataSourcesAndAddNewOnes() {
        RoutableDataSourcesLoader loader = mock(RoutableDataSourcesLoader.class);
        var updatedSources = new RoutableDataSources<>(Map.of(
            1L, TestDataSourceLoader.createDataSource("jdbc:h2:mem:database1a", "user1", "password1"),
            2L, TestDataSourceLoader.createDataSource("jdbc:h2:mem:database2", "user2a", "password2"),
            3L, TestDataSourceLoader.createDataSource("jdbc:h2:mem:database3", "user3", "password3a"),
            4L, TestDataSourceLoader.createDataSource("jdbc:h2:mem:database4", "user4", "password4")
        ));
        when(loader.loadDataSources())
            .thenReturn(TestDataSourceLoader.createDataSources(1, 2, 3))
            .thenReturn(updatedSources);
        RoutableDataSource ds = new RoutableDataSource(loader);

        ds.refresh();

        assertThat(ds.getNumberOfDataSources()).isEqualTo(4);

        DatabaseContextHolder.setDatabaseId(1L);
        var ds1 = (HikariDataSource) ds.determineTargetDataSource();
        assertThat(ds1.getJdbcUrl()).isEqualTo("jdbc:h2:mem:database1a");
        assertThat(ds1.getUsername()).isEqualTo("user1");
        assertThat(ds1.getPassword()).isEqualTo("password1");
        assertThat(ds1.getDriverClassName()).isEqualTo("org.h2.Driver");

        DatabaseContextHolder.setDatabaseId(2L);
        var ds2 = (HikariDataSource) ds.determineTargetDataSource();
        assertThat(ds2.getJdbcUrl()).isEqualTo("jdbc:h2:mem:database2");
        assertThat(ds2.getUsername()).isEqualTo("user2a");
        assertThat(ds2.getPassword()).isEqualTo("password2");
        assertThat(ds2.getDriverClassName()).isEqualTo("org.h2.Driver");

        DatabaseContextHolder.setDatabaseId(3L);
        var ds3 = (HikariDataSource) ds.determineTargetDataSource();
        assertThat(ds3.getJdbcUrl()).isEqualTo("jdbc:h2:mem:database3");
        assertThat(ds3.getUsername()).isEqualTo("user3");
        assertThat(ds3.getPassword()).isEqualTo("password3a");
        assertThat(ds3.getDriverClassName()).isEqualTo("org.h2.Driver");

        DatabaseContextHolder.setDatabaseId(4L);
        var ds4 = (HikariDataSource) ds.determineTargetDataSource();
        assertThat(ds4.getJdbcUrl()).isEqualTo("jdbc:h2:mem:database4");
        assertThat(ds4.getUsername()).isEqualTo("user4");
        assertThat(ds4.getPassword()).isEqualTo("password4");
        assertThat(ds4.getDriverClassName()).isEqualTo("org.h2.Driver");
    }

    static class RoutableDataSourceTestCase {
        Long databaseId;
        Long expectedResult;

        RoutableDataSourceTestCase(Long databaseId, Long expectedResult) {
            this.databaseId = databaseId;
            this.expectedResult = expectedResult;
        }
    }

    static class RoutableDataSourceUrlTestCase {
        Long databaseId;
        String expectedUrl;

        RoutableDataSourceUrlTestCase(Long databaseId, String expectedUrl) {
            this.databaseId = databaseId;
            this.expectedUrl = expectedUrl;
        }
    }
}
