package com.stano.domain_jpa.springjdbc.sql;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ParametersTest {

    @Test
    void shouldBeAbleSetAllParameterTypesAndGetMapOfValuesBack() {
        Sql sql = mock(Sql.class);
        when(sql.getBooleanMode()).thenReturn(Sql.BooleanMode.NATIVE);

        Parameters parameters = new Parameters(sql);
        parameters.setParameter("1", "ABC");
        parameters.setParameter("2", 12345);
        parameters.setParameter("3", Long.MAX_VALUE);
        parameters.setParameter("4", Integer.valueOf(654321));
        parameters.setParameter("5", Long.valueOf(8877665544332211L));
        parameters.setParameter("6", new java.math.BigDecimal("12345.6789"));
        parameters.setParameter("7", true);
        parameters.setParameter("8", LocalDate.of(1996, 10, 6));
        parameters.setParameter("9", LocalDateTime.of(1996, 10, 6, 8, 30, 45));
        parameters.setParameter("10", LocalTime.of(8, 30, 45));
        parameters.setParameter("11", java.util.Arrays.asList(1, 2, 3, 4));

        Map<String, Object> map = parameters.getParameterMap();

        assertThat(map).hasSize(11);
        assertThat(map.get("1")).isEqualTo("ABC");
        assertThat(map.get("2")).isEqualTo(12345);
        assertThat(map.get("3")).isEqualTo(Long.MAX_VALUE);
        assertThat(map.get("4")).isEqualTo(Integer.valueOf(654321));
        assertThat(map.get("5")).isEqualTo(Long.valueOf(8877665544332211L));
        assertThat(map.get("6")).isEqualTo(new java.math.BigDecimal("12345.6789"));
        assertThat(map.get("7")).isEqualTo(true);
        assertThat(map.get("8")).isEqualTo(LocalDate.of(1996, 10, 6));
        assertThat(map.get("9")).isEqualTo(LocalDateTime.of(1996, 10, 6, 8, 30, 45));
        assertThat(map.get("10")).isEqualTo(LocalTime.of(8, 30, 45));
        assertThat(map.get("11")).isEqualTo(java.util.Arrays.asList(1, 2, 3, 4));
    }

    @Test
    void shouldBeAbleSetBooleansAsTrueOrFalseNativeBooleanValuesIfBooleanModeIsNotSet() {
        Sql sql = mock(Sql.class);
        Parameters parameters = new Parameters(sql);
        parameters.setParameter("false_value", false);
        parameters.setParameter("true_value", true);
        parameters.setParameter("false_value_Boolean", Boolean.FALSE);
        parameters.setParameter("true_value_Boolean", Boolean.TRUE);

        Map<String, Object> map = parameters.getParameterMap();

        assertThat(map).hasSize(4);
        assertThat(map.get("false_value")).isEqualTo(Boolean.FALSE);
        assertThat(map.get("true_value")).isEqualTo(Boolean.TRUE);
        assertThat(map.get("false_value_Boolean")).isEqualTo(Boolean.FALSE);
        assertThat(map.get("true_value_Boolean")).isEqualTo(Boolean.TRUE);
    }

    @Test
    void shouldBeAbleSetBooleansAsTrueOrFalseNativeBooleanValuesIfBooleanModeIsNATIVE() {
        Sql sql = mock(Sql.class);
        when(sql.getBooleanMode()).thenReturn(Sql.BooleanMode.NATIVE);

        Parameters parameters = new Parameters(sql);
        parameters.setParameter("false_value", false);
        parameters.setParameter("true_value", true);
        parameters.setParameter("false_value_Boolean", Boolean.FALSE);
        parameters.setParameter("true_value_Boolean", Boolean.TRUE);

        Map<String, Object> map = parameters.getParameterMap();

        assertThat(map).hasSize(4);
        assertThat(map.get("false_value")).isEqualTo(Boolean.FALSE);
        assertThat(map.get("true_value")).isEqualTo(Boolean.TRUE);
        assertThat(map.get("false_value_Boolean")).isEqualTo(Boolean.FALSE);
        assertThat(map.get("true_value_Boolean")).isEqualTo(Boolean.TRUE);
    }

    @Test
    void shouldBeAbleSetBooleansAsYOrNIfBooleanModeIsYN() {
        Sql sql = mock(Sql.class);
        when(sql.getBooleanMode()).thenReturn(Sql.BooleanMode.YN);

        Parameters parameters = new Parameters(sql);
        parameters.setParameter("false_value", false);
        parameters.setParameter("true_value", true);
        parameters.setParameter("false_value_Boolean", Boolean.FALSE);
        parameters.setParameter("true_value_Boolean", Boolean.TRUE);

        Map<String, Object> map = parameters.getParameterMap();

        assertThat(map).hasSize(4);
        assertThat(map.get("false_value")).isEqualTo("N");
        assertThat(map.get("true_value")).isEqualTo("Y");
        assertThat(map.get("false_value_Boolean")).isEqualTo("N");
        assertThat(map.get("true_value_Boolean")).isEqualTo("Y");
    }

    @Test
    void shouldBeAbleSetBooleansAsYesOrNoIfBooleanModeIsYES_NO() {
        Sql sql = mock(Sql.class);
        when(sql.getBooleanMode()).thenReturn(Sql.BooleanMode.YES_NO);

        Parameters parameters = new Parameters(sql);
        parameters.setParameter("false_value", false);
        parameters.setParameter("true_value", true);
        parameters.setParameter("false_value_Boolean", Boolean.FALSE);
        parameters.setParameter("true_value_Boolean", Boolean.TRUE);

        Map<String, Object> map = parameters.getParameterMap();

        assertThat(map).hasSize(4);
        assertThat(map.get("false_value")).isEqualTo("No");
        assertThat(map.get("true_value")).isEqualTo("Yes");
        assertThat(map.get("false_value_Boolean")).isEqualTo("No");
        assertThat(map.get("true_value_Boolean")).isEqualTo("Yes");
    }

    @Test
    void getParameterMapShouldReturnUnmodifiableMap() {
        Sql sql = mock(Sql.class);
        Parameters parameters = new Parameters(sql);

        assertThatThrownBy(() -> parameters.getParameterMap().clear())
            .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void modificationsToParametersAfterCallingGetParameterMapShouldNotImpactReturnedMap() {
        Sql sql = mock(Sql.class);
        Parameters parameters = new Parameters(sql);
        parameters.setParameter("1", "ABC");
        parameters.setParameter("2", 12345);

        Map<String, Object> map = parameters.getParameterMap();

        parameters.setParameter("3", Long.MAX_VALUE);

        assertThat(map).hasSize(2);
        assertThat(map.get("1")).isEqualTo("ABC");
        assertThat(map.get("2")).isEqualTo(12345);
    }
}
