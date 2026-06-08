package com.stano.domain_jpa.springjdbc.sql;

import com.stano.domain_jpa.springjdbc.datasource.ConnectionDataSource;
import com.stano.domain_jpa.springjdbc.datasource.DelegatingConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SqlTest {
    private Connection connection;
    private DataSource dataSource;
    private Sql sql;

    @BeforeEach
    void setup() throws Exception {
        connection = DriverManager.getConnection("jdbc:h2:mem:", "SA", "");
        dataSource = new ConnectionDataSource(new DelegatingConnection(connection));
        sql = new Sql(new JdbcTemplate(dataSource));

        sql.update("create table test (test_pk int,int_value int,long_value bigint,decimal_value decimal(19,4),string_value varchar(100),date_value date,date_time_value timestamp,time_value time,bit_boolean bit,native_boolean boolean,yn_boolean char(1),yes_no_boolean varchar(3),size varchar(6),size_with_code varchar(1),byte_data varbinary(200))")
            .executeUpdate();

        for (int i = 1; i <= 5; i++) {
            sql.update("insert into test (test_pk,int_value,long_value,decimal_value,string_value,date_value,date_time_value,time_value,bit_boolean,native_boolean,yn_boolean,yes_no_boolean,size,size_with_code,byte_data) values (:test_pk,:int_value,:long_value,:decimal_value,:string_value,:date_value,:date_time_value,:time_value,:bit_boolean,:native_boolean,:yn_boolean,:yes_no_boolean,:size,:size_with_code,:byte_data)")
                .setParameter("test_pk", i)
                .setParameter("int_value", Integer.MAX_VALUE)
                .setParameter("long_value", Long.MAX_VALUE)
                .setParameter("decimal_value", new java.math.BigDecimal("123456.7895"))
                .setParameter("string_value", "Test " + i)
                .setParameter("date_value", LocalDate.of(2018, 10, 5 + i))
                .setParameter("date_time_value", LocalDateTime.of(2018, 10, 6, 8, 30, 45))
                .setParameter("time_value", LocalTime.of(16, 30, 45))
                .setParameter("bit_boolean", i % 2 == 0 ? 1 : 0)
                .setParameter("native_boolean", i % 2 == 0)
                .setParameter("yn_boolean", i % 2 == 0 ? "Y" : "N")
                .setParameter("yes_no_boolean", i % 2 == 0 ? "Yes" : "No")
                .setParameter("size", "MEDIUM")
                .setParameter("size_with_code", "L")
                .setParameter("byte_data", new byte[]{5, 6, 7})
                .executeUpdate();
        }
    }

    @AfterEach
    void cleanup() throws Exception {
        connection.close();
    }

    @Test
    void verifyThatWeCanPerformQueries() {
        List<TestData> results = sql.query("select * from test where date_value = :date_value", TestData.class)
            .setParameter("date_value", LocalDate.of(2018, 10, 6))
            .getResultList();

        var resultPk1 = sql.query("select * from test where test_pk = 1", ConstructorRowMapper.newInstance(TestData2.class)).getSingleResult();
        var resultPk2 = sql.query("select * from test where test_pk = 2", ConstructorRowMapper.newInstance(TestData2.class)).getSingleResult();
        var noResult = sql.query("select * from test where test_pk < 0", TestData.class).getSingleResult();

        int count = sql.queryForSingleResult("select count(*) from test", Integer.class).getSingleResult();

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTestPk()).isEqualTo(1);
        assertThat(results.get(0).getIntValue()).isEqualTo(Integer.MAX_VALUE);
        assertThat(results.get(0).getLongValue()).isEqualTo(Long.MAX_VALUE);
        assertThat(results.get(0).getDecimalValue()).isEqualTo(new java.math.BigDecimal("123456.7895"));
        assertThat(results.get(0).getStringValue()).isEqualTo("Test 1");
        assertThat(results.get(0).getDateValue()).isEqualTo(LocalDate.of(2018, 10, 6));
        assertThat(results.get(0).getDateTimeValue()).isEqualTo(LocalDateTime.of(2018, 10, 6, 8, 30, 45));
        assertThat(results.get(0).isBitBoolean()).isFalse();
        assertThat(results.get(0).isNativeBoolean()).isFalse();
        assertThat(results.get(0).isYnBoolean()).isFalse();
        assertThat(results.get(0).isYesNoBoolean()).isFalse();
        assertThat(results.get(0).getSize()).isEqualTo(Size.MEDIUM);
        assertThat(results.get(0).getSizeWithCode()).isEqualTo(SizeWithCode.LARGE);
        assertThat(results.get(0).getByteData()).isEqualTo(new byte[]{5, 6, 7});

        assertThat(resultPk1.isPresent()).isTrue();
        assertThat(resultPk1.get().getTestPk()).isEqualTo(1);
        assertThat(resultPk1.get().getIntValue()).isEqualTo(Integer.MAX_VALUE);
        assertThat(resultPk1.get().getLongValue()).isEqualTo(Long.MAX_VALUE);
        assertThat(resultPk1.get().getDecimalValue()).isEqualTo(new java.math.BigDecimal("123456.7895"));
        assertThat(resultPk1.get().getStringValue()).isEqualTo("Test 1");
        assertThat(resultPk1.get().getDateValue()).isEqualTo(LocalDate.of(2018, 10, 6));
        assertThat(resultPk1.get().getDateTimeValue()).isEqualTo(LocalDateTime.of(2018, 10, 6, 8, 30, 45));
        assertThat(resultPk1.get().isBitBoolean()).isFalse();
        assertThat(resultPk1.get().isNativeBoolean()).isFalse();
        assertThat(resultPk1.get().isYnBoolean()).isFalse();
        assertThat(resultPk1.get().isYesNoBoolean()).isFalse();
        assertThat(resultPk1.get().getSize()).isEqualTo(Size.MEDIUM);
        assertThat(resultPk1.get().getSizeWithCode()).isEqualTo(SizeWithCode.LARGE);
        assertThat(resultPk1.get().getByteData()).isEqualTo(new byte[]{5, 6, 7});

        assertThat(resultPk2.isPresent()).isTrue();
        assertThat(resultPk2.get().getTestPk()).isEqualTo(2);
        assertThat(resultPk2.get().getIntValue()).isEqualTo(Integer.MAX_VALUE);
        assertThat(resultPk2.get().getLongValue()).isEqualTo(Long.MAX_VALUE);
        assertThat(resultPk2.get().getDecimalValue()).isEqualTo(new java.math.BigDecimal("123456.7895"));
        assertThat(resultPk2.get().getStringValue()).isEqualTo("Test 2");
        assertThat(resultPk2.get().getDateValue()).isEqualTo(LocalDate.of(2018, 10, 7));
        assertThat(resultPk2.get().getDateTimeValue()).isEqualTo(LocalDateTime.of(2018, 10, 6, 8, 30, 45));
        assertThat(resultPk2.get().isBitBoolean()).isTrue();
        assertThat(resultPk2.get().isNativeBoolean()).isTrue();
        assertThat(resultPk2.get().isYnBoolean()).isTrue();
        assertThat(resultPk2.get().isYesNoBoolean()).isTrue();
        assertThat(resultPk2.get().getSize()).isEqualTo(Size.MEDIUM);
        assertThat(resultPk2.get().getSizeWithCode()).isEqualTo(SizeWithCode.LARGE);
        assertThat(resultPk2.get().getByteData()).isEqualTo(new byte[]{5, 6, 7});

        assertThat(noResult.isPresent()).isFalse();

        assertThat(count).isEqualTo(5);
    }

    @Test
    void verifyThatWeCanPassCollectionsAsNamedParameters() {
        List<TestData> resultsForGivenLists = sql.query("select * from test where test_pk in (:test_pks)", TestData.class)
            .setParameter("test_pks", java.util.Arrays.asList(1, 2, 3))
            .getResultList();

        List<TestData> resultsForGivenSets = sql.query("select * from test where test_pk in (:test_pks)", TestData.class)
            .setParameter("test_pks", Set.of(1, 4))
            .getResultList();

        assertThat(resultsForGivenLists).hasSize(3);
        assertThat(resultsForGivenLists.get(0).getTestPk()).isEqualTo(1);
        assertThat(resultsForGivenLists.get(1).getTestPk()).isEqualTo(2);
        assertThat(resultsForGivenLists.get(2).getTestPk()).isEqualTo(3);

        assertThat(resultsForGivenSets).hasSize(2);
        assertThat(resultsForGivenSets.get(0).getTestPk()).isEqualTo(1);
        assertThat(resultsForGivenSets.get(1).getTestPk()).isEqualTo(4);
    }
}
