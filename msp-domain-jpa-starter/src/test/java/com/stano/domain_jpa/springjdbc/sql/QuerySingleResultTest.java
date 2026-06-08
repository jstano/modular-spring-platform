package com.stano.domain_jpa.springjdbc.sql;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class QuerySingleResultTest {

    @Test
    void setParameterMethodsShouldWork() {
        NamedParameterJdbcTemplate jdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        Sql sql = mock(Sql.class);

        QuerySingleResult<Long> querySingleResult = new QuerySingleResult<>(sql, jdbcTemplate, "select * from test", Long.class);
        assertThat(querySingleResult.setParameter("string_value", "abc123")).isEqualTo(querySingleResult);
        assertThat(querySingleResult.setParameter("int_value", Integer.MAX_VALUE)).isEqualTo(querySingleResult);
        assertThat(querySingleResult.setParameter("Integer_value", Integer.valueOf(123456))).isEqualTo(querySingleResult);
        assertThat(querySingleResult.setParameter("long_value", Long.MAX_VALUE)).isEqualTo(querySingleResult);
        assertThat(querySingleResult.setParameter("Long_value", Long.valueOf(8877661122L))).isEqualTo(querySingleResult);
        assertThat(querySingleResult.setParameter("decimal_value", new BigDecimal("12345.6789"))).isEqualTo(querySingleResult);
        assertThat(querySingleResult.setParameter("date_value", LocalDate.of(2018, 10, 6))).isEqualTo(querySingleResult);
        assertThat(querySingleResult.setParameter("datetime_value", LocalDateTime.of(2018, 10, 6, 8, 30, 45))).isEqualTo(querySingleResult);
        assertThat(querySingleResult.setParameter("time_value", LocalTime.of(16, 30, 45))).isEqualTo(querySingleResult);
        assertThat(querySingleResult.setParameter("boolean_value", true)).isEqualTo(querySingleResult);
        assertThat(querySingleResult.setParameter("collection_value", java.util.Arrays.asList(1, 2, 3))).isEqualTo(querySingleResult);
        querySingleResult.setParameter("byte_array", new byte[]{5, 6, 7});

        Map<String, Object> paramMap = querySingleResult.parameters.getParameterMap();

        assertThat(paramMap.get("string_value")).isEqualTo("abc123");
        assertThat(paramMap.get("int_value")).isEqualTo(Integer.MAX_VALUE);
        assertThat(paramMap.get("Integer_value")).isEqualTo(Integer.valueOf(123456));
        assertThat(paramMap.get("long_value")).isEqualTo(Long.MAX_VALUE);
        assertThat(paramMap.get("Long_value")).isEqualTo(Long.valueOf(8877661122L));
        assertThat(paramMap.get("decimal_value")).isEqualTo(new BigDecimal("12345.6789"));
        assertThat(paramMap.get("date_value")).isEqualTo(LocalDate.of(2018, 10, 6));
        assertThat(paramMap.get("datetime_value")).isEqualTo(LocalDateTime.of(2018, 10, 6, 8, 30, 45));
        assertThat(paramMap.get("time_value")).isEqualTo(LocalTime.of(16, 30, 45));
        assertThat(paramMap.get("boolean_value")).isEqualTo(true);
        assertThat(paramMap.get("collection_value")).isEqualTo(java.util.Arrays.asList(1, 2, 3));
        assertThat(paramMap.get("byte_array")).isEqualTo(new byte[]{5, 6, 7});
    }
}
