package com.stano.domain_jpa.jpa.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.math.BigDecimal;
import org.javamoney.moneta.Money;

@Converter(autoApply = true)
public class MoneyAttributeConverter implements AttributeConverter<Money, String> {
  @Override
  public String convertToDatabaseColumn(Money attribute) {
    if (attribute == null) {
      return null;
    }
    return attribute.getCurrency().getCurrencyCode()
        + " "
        + attribute.getNumber().numberValue(BigDecimal.class).toPlainString();
  }

  @Override
  public Money convertToEntityAttribute(String dbData) {
    if (dbData == null) {
      return null;
    }
    int spaceIndex = dbData.indexOf(' ');
    String currencyCode = dbData.substring(0, spaceIndex);
    BigDecimal amount = new BigDecimal(dbData.substring(spaceIndex + 1));
    return Money.of(amount, currencyCode);
  }
}
