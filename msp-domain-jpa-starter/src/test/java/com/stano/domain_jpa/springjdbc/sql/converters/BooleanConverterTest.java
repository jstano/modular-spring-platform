package com.stano.domain_jpa.springjdbc.sql.converters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BooleanConverterTest {

    static Stream<BooleanConverterTestCase> booleanConverterTestCases() {
        return Stream.of(
            new BooleanConverterTestCase(Boolean.class, "y", Boolean.TRUE),
            new BooleanConverterTestCase(Boolean.class, "Y", Boolean.TRUE),
            new BooleanConverterTestCase(Boolean.class, "yes", Boolean.TRUE),
            new BooleanConverterTestCase(Boolean.class, "Yes", Boolean.TRUE),
            new BooleanConverterTestCase(Boolean.class, "YES", Boolean.TRUE),
            new BooleanConverterTestCase(Boolean.class, "true", Boolean.TRUE),
            new BooleanConverterTestCase(Boolean.class, "True", Boolean.TRUE),
            new BooleanConverterTestCase(Boolean.class, "TRUE", Boolean.TRUE),
            new BooleanConverterTestCase(Boolean.class, "1", Boolean.TRUE),
            new BooleanConverterTestCase(Boolean.class, "n", Boolean.FALSE),
            new BooleanConverterTestCase(Boolean.class, "no", Boolean.FALSE),
            new BooleanConverterTestCase(Boolean.class, "No", Boolean.FALSE),
            new BooleanConverterTestCase(Boolean.class, "NO", Boolean.FALSE),
            new BooleanConverterTestCase(Boolean.class, "false", Boolean.FALSE),
            new BooleanConverterTestCase(Boolean.class, "False", Boolean.FALSE),
            new BooleanConverterTestCase(Boolean.class, "FALSE", Boolean.FALSE),
            new BooleanConverterTestCase(Boolean.class, "0", Boolean.FALSE),
            new BooleanConverterTestCase(Boolean.class, null, null),
            new BooleanConverterTestCase(boolean.class, "y", Boolean.TRUE),
            new BooleanConverterTestCase(boolean.class, "Y", Boolean.TRUE),
            new BooleanConverterTestCase(boolean.class, "yes", Boolean.TRUE),
            new BooleanConverterTestCase(boolean.class, "Yes", Boolean.TRUE),
            new BooleanConverterTestCase(boolean.class, "YES", Boolean.TRUE),
            new BooleanConverterTestCase(boolean.class, "true", Boolean.TRUE),
            new BooleanConverterTestCase(boolean.class, "True", Boolean.TRUE),
            new BooleanConverterTestCase(boolean.class, "TRUE", Boolean.TRUE),
            new BooleanConverterTestCase(boolean.class, "1", Boolean.TRUE),
            new BooleanConverterTestCase(boolean.class, "n", Boolean.FALSE),
            new BooleanConverterTestCase(boolean.class, "no", Boolean.FALSE),
            new BooleanConverterTestCase(boolean.class, "No", Boolean.FALSE),
            new BooleanConverterTestCase(boolean.class, "NO", Boolean.FALSE),
            new BooleanConverterTestCase(boolean.class, "false", Boolean.FALSE),
            new BooleanConverterTestCase(boolean.class, "False", Boolean.FALSE),
            new BooleanConverterTestCase(boolean.class, "FALSE", Boolean.FALSE),
            new BooleanConverterTestCase(boolean.class, "0", Boolean.FALSE),
            new BooleanConverterTestCase(boolean.class, null, Boolean.FALSE)
        );
    }

    @ParameterizedTest
    @MethodSource("booleanConverterTestCases")
    void shouldConvertStringsToProperBooleanValues(BooleanConverterTestCase testCase) throws SQLException {
        int index = 1;
        ResultSet rs = mock(ResultSet.class);
        when(rs.getObject(index)).thenReturn(testCase.stringValue);

        BooleanConverter booleanConverter = new BooleanConverter();

        Object result = booleanConverter.convertBooleanValue(rs, index, testCase.propertyType);

        assertThat(result).isEqualTo(testCase.expectedResult);
    }

    static class BooleanConverterTestCase {
        Class<?> propertyType;
        String stringValue;
        Object expectedResult;

        BooleanConverterTestCase(Class<?> propertyType, String stringValue, Object expectedResult) {
            this.propertyType = propertyType;
            this.stringValue = stringValue;
            this.expectedResult = expectedResult;
        }
    }
}
