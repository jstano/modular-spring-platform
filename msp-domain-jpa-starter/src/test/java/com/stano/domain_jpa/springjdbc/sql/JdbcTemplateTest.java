package com.stano.domain_jpa.springjdbc.sql;

import com.stano.domain_jpa.springjdbc.datasource.ConnectionDataSource;
import com.stano.domain_jpa.springjdbc.datasource.DelegatingConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcTemplateTest {
    private Connection connection;
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() throws Exception {
        connection = DriverManager.getConnection("jdbc:h2:mem:", "SA", "");
        dataSource = new ConnectionDataSource(new DelegatingConnection(connection));
        jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.execute("create table test (test_pk int not null,int_value int,date_value date)");
        new NamedParameterJdbcTemplate(jdbcTemplate).update(
            "insert into test (test_pk,int_value,date_value) values (:test_pk,:int_value,:date_value)",
            java.util.Map.of(
                "test_pk", 1,
                "int_value", 123456,
                "date_value", LocalDate.of(2018, 10, 6)
            )
        );
    }

    @AfterEach
    void cleanup() throws Exception {
        connection.close();
    }

    @Test
    void verifyThatWeCanPerformQueries() {
        List<TestData> results = jdbcTemplate.query("select * from test", new CustomBeanPropertyRowMapper<>(TestData.class));
        Integer count = jdbcTemplate.queryForObject("select count(*) from test", Integer.class);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTestPk()).isEqualTo(1);
        assertThat(results.get(0).getIntValue()).isEqualTo(123456);
        assertThat(results.get(0).getDateValue()).isEqualTo(LocalDate.of(2018, 10, 6));
        assertThat(count).isEqualTo(1);
    }
}
