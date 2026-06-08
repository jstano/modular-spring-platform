package com.stano.domain_jpa.springjdbc.sql;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class QueryTest {

    @Test
    void setParameterMethodsShouldWork() {
        Sql sql = mock(Sql.class);
        NamedParameterJdbcTemplate jdbcTemplate = mock(NamedParameterJdbcTemplate.class);

        Query<TestData> query = new Query<>(sql, jdbcTemplate, "select * from test", new CustomBeanPropertyRowMapper<>(TestData.class));
        query.setParameter("string_value", "abc123");
        query.setParameter("int_value", Integer.MAX_VALUE);
        query.setParameter("long_value", Long.MAX_VALUE);
        query.setParameter("decimal_value", BigDecimal.TEN);
        query.setParameter("date_value", LocalDate.of(2018, 10, 6));
        query.setParameter("datetime_value", LocalDateTime.of(2018, 10, 6, 8, 30, 45));
        query.setParameter("time_value", LocalTime.of(16, 30, 45));
        query.setParameter("integer_values", java.util.Arrays.asList(1, 2));
        query.setParameter("integer_set", Set.of(1, 2));
        query.setParameter("byte_array", new byte[]{5, 6, 7});

        Map<String, Object> paramMap = query.parameters.getParameterMap();

        assertThat(paramMap.get("string_value")).isEqualTo("abc123");
        assertThat(paramMap.get("int_value")).isEqualTo(Integer.MAX_VALUE);
        assertThat(paramMap.get("long_value")).isEqualTo(Long.MAX_VALUE);
        assertThat(paramMap.get("decimal_value")).isEqualTo(BigDecimal.TEN);
        assertThat(paramMap.get("date_value")).isEqualTo(LocalDate.of(2018, 10, 6));
        assertThat(paramMap.get("datetime_value")).isEqualTo(LocalDateTime.of(2018, 10, 6, 8, 30, 45));
        assertThat(paramMap.get("time_value")).isEqualTo(LocalTime.of(16, 30, 45));
        assertThat(paramMap.get("integer_values")).isEqualTo(java.util.Arrays.asList(1, 2));
        assertThat(paramMap.get("integer_set")).isEqualTo(Set.of(1, 2));
        assertThat(paramMap.get("byte_array")).isEqualTo(new byte[]{5, 6, 7});
    }
}
