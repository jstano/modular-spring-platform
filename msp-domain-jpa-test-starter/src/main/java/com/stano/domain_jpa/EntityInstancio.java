package com.stano.domain_jpa;

import com.stano.crypto.binary.EncryptedBytes;
import com.stano.crypto.binary.EncryptedBytesFactory;
import com.stano.crypto.password.Password;
import com.stano.crypto.password.PasswordFactory;
import com.stano.crypto.text.EncryptedText;
import com.stano.crypto.text.EncryptedTextFactory;
import com.stano.domain_jpa.entity.AbstractEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Currency;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.Select;

public class EntityInstancio {
  public static <T extends AbstractEntity<?>> InstancioApi<T> of(Class<T> type) {
    return Instancio.of(type)
        .ignore(Select.fields().named("id").declaredIn(AbstractEntity.class))
        .ignore(Select.fields().named("createdAt").declaredIn(AbstractEntity.class))
        .ignore(Select.fields().named("updatedAt").declaredIn(AbstractEntity.class))
        .ignore(Select.fields().named("version").declaredIn(AbstractEntity.class))
        .supply(
            Select.all(LocalDateTime.class),
            random ->
                LocalDateTime.of(
                    LocalDate.ofEpochDay(random.longRange(-36500, 36500)),
                    LocalTime.of(
                        random.intRange(0, 23),
                        random.intRange(0, 59),
                        random.intRange(0, 59),
                        random.intRange(0, 999_999) * 1000)))
        .supply(
            Select.all(LocalTime.class),
            random ->
                LocalTime.of(
                    random.intRange(0, 23), random.intRange(0, 59), random.intRange(0, 59)))
        .supply(
            Select.all(EncryptedBytes.class),
            random ->
                EncryptedBytesFactory.getInstance()
                    .withClearBytes(random.alphanumeric(16).getBytes()))
        .supply(
            Select.all(EncryptedText.class),
            random -> EncryptedTextFactory.getInstance().withClearText(random.alphanumeric(16)))
        .supply(
            Select.all(Password.class),
            random -> PasswordFactory.getInstance().withClearText(random.alphanumeric(16)))
        .supply(Select.all(Currency.class), random -> Currency.getInstance("USD"));
  }
}
