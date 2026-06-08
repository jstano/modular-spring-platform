package com.stano.domain_jpa.springjdbc.sql.converters;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EnumConverterTest {

    @Test
    void shouldConvertEnumValuesForRegularBeanUsingGettersAndSetters() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getString(1)).thenReturn("2");
        when(rs.getString(2)).thenReturn("B");
        when(rs.getString(3)).thenReturn(null);
        when(rs.getString(4)).thenReturn(null);

        EnumConverter enumConverter = new EnumConverter(TestDataBean.class);

        assertThat(enumConverter.convertEnumValue(rs, 1, EnumType1.class)).isEqualTo(EnumType1.TWO);
        assertThat(enumConverter.convertEnumValue(rs, 2, EnumType2.class)).isEqualTo(EnumType2.B);
        assertThat(enumConverter.convertEnumValue(rs, 3, EnumType1.class)).isNull();
        assertThat(enumConverter.convertEnumValue(rs, 4, EnumType2.class)).isNull();
    }

    @Test
    void shouldConvertEnumValuesForReadOnlyBeanThatHasConstructorAndOnlyGetters() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getString(1)).thenReturn("2");
        when(rs.getString(2)).thenReturn("B");
        when(rs.getString(3)).thenReturn(null);
        when(rs.getString(4)).thenReturn(null);

        EnumConverter enumConverter = new EnumConverter(TestDataReadOnlyBean.class.getConstructors()[0]);

        assertThat(enumConverter.convertEnumValue(rs, 1, EnumType1.class)).isEqualTo(EnumType1.TWO);
        assertThat(enumConverter.convertEnumValue(rs, 2, EnumType2.class)).isEqualTo(EnumType2.B);
        assertThat(enumConverter.convertEnumValue(rs, 3, EnumType1.class)).isNull();
        assertThat(enumConverter.convertEnumValue(rs, 4, EnumType2.class)).isNull();
    }
}
