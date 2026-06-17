package com.stano.data_source;

import static org.assertj.core.api.Assertions.assertThat;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

class DataSourceFactoryTest {

  @Test
  void createDataSourceAppliesDefaultsWhenNoHikariPropertiesAreSet() {
    var dataSource =
        (HikariDataSource)
            DataSourceFactory.createDataSource(
                new MockEnvironment(), "jdbc:h2:mem:testdb", "sa", "secret");

    assertThat(dataSource.getJdbcUrl()).isEqualTo("jdbc:h2:mem:testdb");
    assertThat(dataSource.getUsername()).isEqualTo("sa");
    assertThat(dataSource.getPassword()).isEqualTo("secret");
    assertThat(dataSource.getMinimumIdle()).isEqualTo(3);
    assertThat(dataSource.getMaximumPoolSize()).isEqualTo(100);
    assertThat(dataSource.getConnectionTimeout()).isEqualTo(30000L);
    assertThat(dataSource.getMaxLifetime()).isEqualTo(1800000L);
    assertThat(dataSource.getKeepaliveTime()).isEqualTo(0L);
  }

  @Test
  void createDataSourceHonorsExplicitOverrides() {
    var environment =
        new MockEnvironment().withProperty("spring.datasource.hikari.maximum-pool-size", "50");

    var dataSource =
        (HikariDataSource)
            DataSourceFactory.createDataSource(environment, "jdbc:h2:mem:testdb", "sa", "secret");

    assertThat(dataSource.getMaximumPoolSize()).isEqualTo(50);
  }

  @Test
  void createDataSourceHonorsHikariPropertiesNotPreviouslySupported() {
    var environment =
        new MockEnvironment().withProperty("spring.datasource.hikari.pool-name", "custom-pool");

    var dataSource =
        (HikariDataSource)
            DataSourceFactory.createDataSource(environment, "jdbc:h2:mem:testdb", "sa", "secret");

    assertThat(dataSource.getPoolName()).isEqualTo("custom-pool");
  }
}
