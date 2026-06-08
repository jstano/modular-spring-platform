package com.stano.domain_jpa.springjdbc.sql;

import org.junit.jupiter.api.Test;
import org.joor.Reflect;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StatementBaseTest {

    @Test
    void shouldBeAbleToCreateStatementBaseObjectAndGetValuesOut() {
        Sql sql = mock(Sql.class);
        when(sql.getBooleanMode()).thenReturn(Sql.BooleanMode.YES_NO);
        NamedParameterJdbcTemplate jdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        String sqlString = "select * from data";

        Update statementBase = new Update(sql, jdbcTemplate, sqlString);

        assertThat(statementBase.getParameters()).isNotNull();
        assertThat((Object) Reflect.on(statementBase).field("sql").get()).isEqualTo(sql);
        assertThat((String) Reflect.on(statementBase).field("sqlString").get()).isEqualTo("select * from data");
        assertThat((Object) Reflect.on(statementBase).field("jdbcTemplate").get()).isInstanceOf(NamedParameterJdbcTemplate.class);
    }
}
