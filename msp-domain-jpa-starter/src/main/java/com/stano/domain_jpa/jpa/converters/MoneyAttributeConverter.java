package com.stano.domain_jpa.jpa.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.math.BigDecimal;
import org.javamoney.moneta.Money;

/**
 * JPA converter that persists a JSR-354 {@link Money} value as a single text column combining the
 * ISO currency code and plain decimal amount, separated by a space (e.g. {@code "USD 12.50"}).
 * Registered with {@code autoApply = true}, so it applies automatically to any entity attribute
 * typed as {@link Money} without further annotation.
 */
@Converter(autoApply = true)
public class MoneyAttributeConverter implements AttributeConverter<Money, String> {
  /**
   * Formats a monetary amount as {@code "<currencyCode> <amount>"} for storage in a text column.
   *
   * @param attribute the amount to convert, may be {@code null}
   * @return the formatted value, or {@code null} if {@code attribute} is {@code null}
   */
  @Override
  public String convertToDatabaseColumn(Money attribute) {
    if (attribute == null) {
      return null;
    }
    return attribute.getCurrency().getCurrencyCode()
        + " "
        + attribute.getNumber().numberValue(BigDecimal.class).toPlainString();
  }

  /**
   * Parses a {@code "<currencyCode> <amount>"} column value back into a {@link Money}.
   *
   * @param dbData the raw {@code "<currencyCode> <amount>"} column value, may be {@code null}
   * @return the parsed monetary amount, or {@code null} if {@code dbData} is {@code null}
   */
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
